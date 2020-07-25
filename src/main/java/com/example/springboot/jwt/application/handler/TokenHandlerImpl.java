package com.example.springboot.jwt.application.handler;


import com.example.springboot.jwt.application.beans.TokenBean;
import com.example.springboot.jwt.application.handler.interfaces.ITokenHandler;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

@Component
public final class TokenHandlerImpl implements ITokenHandler {

    @Value("${jwt.expiration.duration}")
    private int jwtExpirationDuration;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(TokenHandlerImpl.class);

    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @Override
    public UsernamePasswordAuthenticationToken parseUserFromToken(String token) {
        Claims claims = null;
        if (token == null) {
            throw new RuntimeException("Token not found");
        }

        try {
            claims = Jwts.parserBuilder().setSigningKey(getSecret()).build().parseClaimsJws(token).getBody();

            final String username = claims.getSubject();

            final String roles = (String) claims.get("roles");

//            final Person person = personService.findPerson(username);

//            person.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(roles));

            User user = new User(username, "", AuthorityUtils.commaSeparatedStringToAuthorityList(roles));

            return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

        } catch (SignatureException sgtre) {
            sgtre.printStackTrace();
            throw new JwtException("Invalid token");

        } catch (ExpiredJwtException eje) {
            eje.printStackTrace();
            throw new JwtException("Token expried");

        } catch (JwtException jwte) {
            jwte.printStackTrace();
            throw new JwtException("Unauthorised : Invalid user");

        }
    }

    @Override
    public TokenBean createTokenForUser(String user, Collection<? extends GrantedAuthority> roles) {
        Date exp = new Date(System.currentTimeMillis() + (1000 * jwtExpirationDuration * 60));
        final byte[] secretBytes = Base64.encodeBase64(jwtSecret.getBytes());

        final Key signingKey = new SecretKeySpec(secretBytes, signatureAlgorithm.getJcaName());

        return new TokenBean(Jwts.builder()
                .setSubject(user)
                .claim("roles", AuthorityListToCommaSeparatedString(roles))
                .setExpiration(exp)
                .signWith(signingKey, signatureAlgorithm)
                .compact(), exp);
    }

    @Override
    public void killToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSecret()).build().parseClaimsJws(token).getBody();
        claims.setExpiration(new Date());
    }

    private byte[] getSecret() {
        return Base64.encodeBase64(jwtSecret.getBytes());
    }

    private static String AuthorityListToCommaSeparatedString(Collection<? extends GrantedAuthority> authorities) {
        Set<String> authoritiesAsSetOfString = AuthorityUtils.authorityListToSet(authorities);
        return StringUtils.join(authoritiesAsSetOfString, ", ");
    }
}

