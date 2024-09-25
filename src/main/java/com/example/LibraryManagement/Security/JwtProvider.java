package com.example.LibraryManagement.Security;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
public class JwtProvider {
    public static String generateResetPasswordToken(String userEmail) {
        return Jwts.builder()
                .setSubject(userEmail)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JwtConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.forName(JwtConstants.SIGNATURE_ALGORITHM), JwtConstants.SECRET_KEY)
                .compact();
    }

    public static String validateToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JwtConstants.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }
}