package com.example.springboot.jwt.application.filter;

import com.example.springboot.jwt.application.common.Constants;
import com.example.springboot.jwt.application.entity.Token;
import com.example.springboot.jwt.application.handler.CustomAuthenticationFailureHandler;
import com.example.springboot.jwt.application.repository.TokenRepository;
import com.example.springboot.jwt.application.service.interfaces.ITokenAuthenticationService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final ITokenAuthenticationService tokenAuthenticationService;

	@Autowired
	private TokenRepository repository;

	@Autowired
	private CustomAuthenticationFailureHandler customAuthFailureHandler;

	@Autowired
	public JwtAuthenticationFilter(ITokenAuthenticationService tokenAuthenticationService) {
		this.tokenAuthenticationService = tokenAuthenticationService;
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		try {
			Authentication authentication = tokenAuthenticationService.getAuthentication((HttpServletRequest) req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			if (authentication != null) {
				String user = ((UserDetails) authentication.getPrincipal()).getUsername();
				String token = tokenAuthenticationService.getJwtToken((HttpServletRequest) req);
				Token tokenEntity = repository.findByUsernameAndToken(user, token);
				Optional.ofNullable(tokenEntity).orElseThrow(() -> new JwtException("Token is invalid"));
			}
			chain.doFilter(req, res);
		} catch (AuthenticationException | JwtException e) {
			SecurityContextHolder.clearContext();
			((HttpServletResponse) res).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			customAuthFailureHandler.onAuthenticationFailure((HttpServletRequest) req, (HttpServletResponse) res,
					new AuthenticationException(Constants.UNAUTH_RESPONSE, e) {
					});
		}
	}
}
