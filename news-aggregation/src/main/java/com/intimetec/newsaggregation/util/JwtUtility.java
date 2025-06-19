package com.intimetec.newsaggregation.util;

import com.intimetec.newsaggregation.exception.TokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public final class JwtUtility {

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKey;

    public String generateToken(String subject) {
        try {
            return Jwts
                    .builder()
                    .setSubject(subject)
                    .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                    .setExpiration(new Date(System.currentTimeMillis() + 10L * 24 * 60 * 60 * 1000))
                    .compact();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String verifyToken(String token) {
        try {
            Claims claims = Jwts
                    .parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            throw new TokenException("Token Expired!");
        }
    }

    public void checkExpiration(String token) {
        try {
            Jwts
                    .parser()
                    .setSigningKey(jwtSecretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();
        } catch (Exception e) {
            throw new TokenException("Token Expired!");
        }
    }

}
