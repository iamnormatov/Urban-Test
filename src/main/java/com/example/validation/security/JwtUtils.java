package com.example.validation.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

//    @Value(value = "${secret.key}")
    private String secretKey = "hdsabdkjabd,a2318bek2e21hveb2jvle-ff-s-fs-fs0sfskfn";

    public String generateToken(String lesson) {
        return Jwts.builder()
                .setSubject(lesson)
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public boolean isValid(String token) {
        try {
            if (!parser().isSigned(token)) return false;
            return !StringUtils.isBlank(parser()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T getClaims(String token, String claimName, Class<T> type){
        try {
            return parser()
                    .parseClaimsJws(token)
                    .getBody()
                    .get(claimName, type);
        }catch (Exception e){
            return null;
        }
    }

    private JwtParser parser() {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build();

    }
}
