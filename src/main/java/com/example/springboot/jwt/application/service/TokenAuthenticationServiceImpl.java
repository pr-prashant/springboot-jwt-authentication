package com.example.springboot.jwt.application.service;

import com.example.springboot.jwt.application.handler.interfaces.ITokenHandler;
import com.example.springboot.jwt.application.service.interfaces.ITokenAuthenticationService;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
class TokenAuthenticationServiceImpl implements ITokenAuthenticationService {

    private final ITokenHandler tokenHandler;

    @Autowired
    public TokenAuthenticationServiceImpl(ITokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    public Authentication getAuthentication(HttpServletRequest request) throws JwtException {
        String jwt = getJwtToken(request);
/*
        return tokenHandler
                .parseUserFromToken(jwt)
                .map(UserAuthentication::new)
                .orElse(null);*/
        if (StringUtils.isEmpty(jwt)) return null;
        return tokenHandler
                .parseUserFromToken(jwt);
    }

    @Override
    public String getJwtToken(HttpServletRequest request) throws JwtException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        if (!authHeader.startsWith("Bearer")) return null;

        final String jwt = authHeader.substring(7);
        if (jwt.isEmpty()) return null;
        return jwt;
    }
}
