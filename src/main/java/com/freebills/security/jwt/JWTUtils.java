package com.freebills.security.jwt;

import com.freebills.gateways.entities.UserEntity;
import com.freebills.repositories.UserRepository;
import com.freebills.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTUtils.class);

    private final UserRepository userRepository;
    private final Environment environment;

    @Value("${api.app.jwtSecret}")
    private String jwtSecret;

    @Value("${api.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${api.app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        final Optional<UserEntity> findedUser = userRepository.findByLoginIgnoreCase(userPrincipal.getUsername());

        if (findedUser.isPresent()) {
            String jwt = generateTokenFromUsername(findedUser.get());
            final var builder = ResponseCookie.from(jwtCookie, jwt)
                    .path("/").maxAge(3600000L)
                    .secure(true)
                    .httpOnly(true);

            if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
                builder.domain(".wavods.com");
            }

            return builder.build();
        }

        return null;
    }

    public ResponseCookie getCleanJwtCookie() {
        final var builder = ResponseCookie.from(jwtCookie, "")
                .path("/")
                .maxAge(0)
                .secure(true)
                .httpOnly(true);

        if (Arrays.asList(environment.getActiveProfiles()).contains("prod")) {
            builder.domain(".wavods.com");
        }

        return builder.build();
    }

    public String getUserNameFromJwtToken(final String token) {
        final Object email = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().get("email");

        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            LOGGER.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            LOGGER.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            LOGGER.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            LOGGER.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            LOGGER.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public String generateTokenFromUsername(UserEntity userEntity) {
        return Jwts.builder()
                .claim("id", userEntity.getId())
                .setSubject(userEntity.getLogin())
                .claim("name", userEntity.getName())
                .claim("email", userEntity.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
}
