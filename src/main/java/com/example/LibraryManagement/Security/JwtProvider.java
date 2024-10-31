package com.example.LibraryManagement.Security;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + JwtConstants.EXPIRE_DATE);
        String token = Jwts.builder()
                .setSubject(email)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(keys(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    private Key keys() {
        byte[] keyBytes = Decoders.BASE64.decode(JwtConstants.SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(keys())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

    }

    public boolean vailidateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(keys())
                .build()
                .parseClaimsJws(token);
        return true;
    }

    public String getJwtFromCookies(HttpServletRequest request) {
        return getCookieValuesByName(request, JwtConstants.JWT_COOKIE_NAME);
    }

    public String getJwtRefreshFromCookies(HttpServletRequest request) {
        return getCookieValuesByName(request, JwtConstants.REFRESH_TOKEN_COOKIE_NAME);
    }

    public String getCookieValuesByName(HttpServletRequest request, String name) {
        String token = null;
        if (request.getCookies() != null) {
            Cookie[] rc = request.getCookies();
            for (int i = 0; i < rc.length; i++) {
                if (rc[i].getName().equals(name) == true) {
                    token = rc[i].getValue().toString();
                }
            }
        }
        return token;
    }

    public ResponseCookie generateJwtCookie(Object user) {
        String email;
        // Kiểm tra loại người dùng và lấy email
        if (user instanceof Student) {
            email = ((Student) user).getStudentEmail();
        } else if (user instanceof Staff) {
            email = ((Staff) user).getStaffEmail();
        }else if (user instanceof Admin) {
            email = ((Admin) user).getEmail();
        } else {
            throw new IllegalArgumentException("Invalid user type");
        }

        String jwtToken = generateTokenFromEmail(email);
        return generateCookie(JwtConstants.JWT_COOKIE_NAME, jwtToken, "/");
    }

    public String generateTokenFromEmail(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + JwtConstants.EXPIRE_DATE))
                .signWith(keys(), SignatureAlgorithm.HS256)
                .compact();
    }

    public ResponseCookie generateRefreshJwtCookie(String refreshToken) {
        return generateCookie(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, refreshToken, "/library/refresh-token");
    }

    private ResponseCookie generateCookie(String name, String value, String path) {
        ResponseCookie responseCookie = ResponseCookie.from(name, value)
                .path(path)
                .maxAge(24 * 60 * 60)
                .httpOnly(true)
                .build();
        return responseCookie;
    }

    // Method to convert ResponseCookie to Cookie and add to response
    public void addCookieToResponse(HttpServletResponse response, ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setPath(responseCookie.getPath());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setSecure(responseCookie.isSecure());
        response.addCookie(cookie);
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(JwtConstants.JWT_COOKIE_NAME, null).maxAge(0).path("/").build();
        return cookie;
    }

    public ResponseCookie getCleanJwtRefreshCookie() {
        ResponseCookie cookie = ResponseCookie.from(JwtConstants.REFRESH_TOKEN_COOKIE_NAME, null).maxAge(0).path("/library/refresh-token").build();
        return cookie;
    }
}
