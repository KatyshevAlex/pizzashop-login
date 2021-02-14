package com.pizzashop.login.service;

import com.pizzashop.login.configs.JwtProperties;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Autowired
    JwtProperties jwtProperties;

    String encodedKey;

    @PostConstruct
    protected void init() {
        encodedKey = Base64.getEncoder().encodeToString(jwtProperties.getSecretKey().getBytes());
    }

    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + jwtProperties.getValidityInMilliseconds());

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(jwtProperties.getIssuer())
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, encodedKey)
                .compact();
    }

     public boolean validateToken(String token) {

        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(encodedKey)
                    .parseClaimsJws(token);

            return (!claims.getBody().getExpiration().before(new Date()));
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Exception during parsing token - {}. ", token, e);
            return false;
        }
    }

    public String getCustomerLogin(String token) {
        return Jwts.parser().setSigningKey(encodedKey).parseClaimsJws(token).getBody().getSubject();
    }


}
