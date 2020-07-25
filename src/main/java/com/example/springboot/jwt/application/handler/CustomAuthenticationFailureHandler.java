package com.example.springboot.jwt.application.handler;

import com.example.springboot.jwt.application.common.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
	private ObjectMapper responseMapper = new ObjectMapper();

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) throws IOException, ServletException {
		if (Objects.nonNull(response)) {
			Map<String, Object> data = new HashMap<>();
			data.put(Constants.TIME_STAMP, LocalDateTime.now()
					.format(DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_YYY_MM_DD_MMSS)));
			data.put(Constants.STATUS, HttpServletResponse.SC_UNAUTHORIZED);
			data.put(Constants.ERROR, (exception.getCause() instanceof JwtException) ? Constants.UNAUTH_RESPONSE : "");
			data.put(Constants.MESSAGE,
					(exception.getCause() instanceof JwtException) ? Constants.JWT_EXCEPTION_RESPONSE
							: Constants.UNAUTH_RESPONSE);
			data.put(Constants.PATH, request.getRequestURI());
			response.setContentType(Constants.CONTENT_TYPE_JSON);
			response.setCharacterEncoding(Constants.CHAR_ENCODE_UTF);
			response.getWriter().println(responseMapper.writeValueAsString(data));
		}
	}

}
