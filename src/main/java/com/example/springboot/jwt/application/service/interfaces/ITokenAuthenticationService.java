package com.example.springboot.jwt.application.service.interfaces;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface ITokenAuthenticationService {

    Authentication getAuthentication(HttpServletRequest request) throws JwtException;

    String getJwtToken(HttpServletRequest request) throws JwtException;
}

