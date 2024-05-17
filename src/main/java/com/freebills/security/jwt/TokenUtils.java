package com.freebills.security.jwt;

import com.freebills.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class TokenUtils {

    private static final String COOKIE = "token";
    private final Environment environment;

    @Value("${api.app.jwtSecret}")
    private String jwtSecret;

    @Value("${api.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String getTokenFromCookie(HttpServletRequest request) {
        var cookie = WebUtils.getCookie(request, COOKIE);
        return cookie != null ? cookie.getValue() : null;
    }

    public String getUserName(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    private SecretKey getSigningKey(String string) {
        return Keys.hmacShaKeyFor(string.getBytes(StandardCharsets.UTF_8));
    }

    public ResponseCookie generateToken(UserDetailsImpl user) {
        String jwt = generateTokenFromUser(user);
        ResponseCookie.ResponseCookieBuilder builder = ResponseCookie.from(COOKIE, jwt)
                .path("/")
                .maxAge(216_000)
                .secure(true)
                .httpOnly(true);

        if (List.of(environment.getActiveProfiles()).contains("prod")) {
            builder.domain(".wavods.com");
        }

        return builder.build();
    }

    public ResponseCookie cleanToken() {
        return ResponseCookie.from(COOKIE, "")
                .path("/")
                .maxAge(0)
                .secure(true)
                .httpOnly(true)
                .build();
    }

    public String generateTokenFromUser(UserDetailsImpl user) {
        return Jwts.builder()
                .claim("id", user.getId())
                .subject(user.getUsername())
                .claim("name", user.getUsername())
                .claim("email", user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(getSigningKey(jwtSecret))
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(getSigningKey(jwtSecret)).build().parseSignedClaims(token);
            return true;
        } catch (WeakKeyException | SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
