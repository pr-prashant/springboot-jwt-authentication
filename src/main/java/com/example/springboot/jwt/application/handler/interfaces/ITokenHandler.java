package com.example.springboot.jwt.application.handler.interfaces;

import com.example.springboot.jwt.application.beans.TokenBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public interface ITokenHandler {

    UsernamePasswordAuthenticationToken parseUserFromToken(String token);

    TokenBean createTokenForUser(String user, Collection<? extends GrantedAuthority> roles);

    void killToken(String token);
}
