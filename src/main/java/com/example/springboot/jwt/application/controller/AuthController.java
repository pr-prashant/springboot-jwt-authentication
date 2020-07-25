package com.example.springboot.jwt.application.controller;

import com.example.springboot.jwt.application.beans.LoginBean;
import com.example.springboot.jwt.application.beans.TokenBean;
import com.example.springboot.jwt.application.common.RestResponse;
import com.example.springboot.jwt.application.common.exceptions.GenericServiceException;
import com.example.springboot.jwt.application.common.interfaces.IRestResponse;
import com.example.springboot.jwt.application.entity.Token;
import com.example.springboot.jwt.application.handler.interfaces.ITokenHandler;
import com.example.springboot.jwt.application.repository.TokenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(ControllerConstants.SERVICE_ROOT)
public class AuthController {

	private Logger logger = LogManager.getLogger(AuthController.class);

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private ITokenHandler tokenHandler;

	@Autowired
	private TokenRepository tokenRepository;

	@RequestMapping(value = ControllerConstants.LOGIN,
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<? extends IRestResponse> authenticateUser(@RequestBody LoginBean login) throws GenericServiceException {
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		RestResponse<String> response = new RestResponse<>();
		try {
			// Authenticate through LDAP/AD
			final UsernamePasswordAuthenticationToken loginToken = login.toAuthenticationToken();
			final Authentication authentication = authenticationManager.authenticate(loginToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			if (authentication.isAuthenticated()) {
				logger.info("Principal:" + authentication.getPrincipal());
				// Prepare JWT token
				TokenBean token = tokenHandler.createTokenForUser(login.getUsername(), ((UserDetails) authentication.getPrincipal()).getAuthorities());
				Token tokenEntity = new Token();
				tokenEntity.setToken(token.getToken());
				tokenEntity.setUsername(login.getUsername());
				tokenEntity.setExpiryTime(token.getExpiryTime());
				tokenRepository.saveAndFlush(tokenEntity);
				response.setData(token.getToken());
				success = true;
			} else {
				status = HttpStatus.UNAUTHORIZED;
				response.setErrorResponse(HttpStatus.UNAUTHORIZED.toString(), "Username or Password is invalid");
			}
		} catch (BadCredentialsException e) {

			status = HttpStatus.UNAUTHORIZED;
			logger.error("Username or Password is invalid", e);
			response.setErrorResponse(HttpStatus.UNAUTHORIZED.toString(), "Username or Password is invalid");

		} catch (CredentialsExpiredException e) {

			status = HttpStatus.UNAUTHORIZED;
			logger.error("Your credentials have expired. Please contact you administrator." + e.getMessage(), e);
			response.setErrorResponse(HttpStatus.UNAUTHORIZED.toString(), "Your credentials have expired. Please contact you administrator.");

		} catch (LockedException e) {

			status = HttpStatus.LOCKED;
			logger.error("Your account is locked. Please contact your administrator." + e.getMessage(), e);
			response.setErrorResponse(HttpStatus.LOCKED.toString(), "Your account is locked. Please contact your administrator.");

		} catch (DisabledException e) {

			status = HttpStatus.UNAUTHORIZED;
			logger.error("Your account is disabled. Please contact your administrator." + e.getMessage(), e);
			response.setErrorResponse(HttpStatus.UNAUTHORIZED.toString(), "Your account is disabled. Please contact your administrator.");

		} catch (Exception e) {

			status = HttpStatus.SERVICE_UNAVAILABLE;
			logger.error("Exception authenticating user", e);
			response.setErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.toString(), e.getMessage());
		}
		response.setSuccess(success);
		return new ResponseEntity<RestResponse<String>>(response, status);
	}

	@RequestMapping(value = ControllerConstants.REFRESH,
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<? extends IRestResponse> refreshToken(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws GenericServiceException {
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		RestResponse<String> response = new RestResponse<>();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication.isAuthenticated()) {
				logger.info("Principal:" + authentication.getPrincipal());
				String username = ((UserDetails) authentication.getPrincipal()).getUsername();
				// Prepare JWT token
				TokenBean token = tokenHandler.createTokenForUser(username, ((UserDetails) authentication.getPrincipal()).getAuthorities());
				tokenRepository.refreshToken(username, token.getToken(), token.getExpiryTime());
				response.setData(token.getToken());
				success = true;
			} else {
				status = HttpStatus.UNAUTHORIZED;
				response.setErrorResponse(HttpStatus.UNAUTHORIZED.toString(), "User is not logged in");
			}
		} catch (Exception e) {
			status = HttpStatus.SERVICE_UNAVAILABLE;
			logger.error("Exception refreshing token", e);
			response.setErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.toString(), "Exception refreshing token");
		}
		response.setSuccess(success);
		return new ResponseEntity<>(response, status);
	}

	@RequestMapping(value = ControllerConstants.LOGOUT,
			method = RequestMethod.POST,
			produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<? extends IRestResponse> logout(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws GenericServiceException {
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		RestResponse<Void> response = new RestResponse<>();
		try {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				String username = ((UserDetails) auth.getPrincipal()).getUsername();
				tokenRepository.deleteByUser(username);
				new SecurityContextLogoutHandler().logout(servletRequest, servletResponse, auth);
			}
			killToken(servletRequest);
			handleLogOutResponse(servletRequest, servletResponse);
			success = true;
		} catch (Exception e) {
			status = HttpStatus.SERVICE_UNAVAILABLE;
			logger.error("Exception logging out the user", e);
			response.setErrorResponse(HttpStatus.SERVICE_UNAVAILABLE.toString(), "Exception logging out the logout");
		}
		response.setSuccess(success);
		return new ResponseEntity<RestResponse<Void>>(response, status);
	}

	private void killToken(HttpServletRequest request) {
		final String authHeader = request.getHeader("Authorization");
		if (authHeader != null && authHeader.startsWith("Bearer")) {
			final String jwt = authHeader.substring(7);
			tokenHandler.killToken(jwt);
		}
	}

	private void handleLogOutResponse(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookie.setMaxAge(0);
				cookie.setValue(null);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
	}
}
