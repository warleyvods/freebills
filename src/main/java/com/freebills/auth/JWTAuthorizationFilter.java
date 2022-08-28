package com.freebills.auth;

import com.freebills.auth.service.CustomUserDetailService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.freebills.auth.SecurityConstants.SECRET;
import static com.freebills.auth.SecurityConstants.TOKEN_PREFIX;


@Slf4j
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final CustomUserDetailService customUserDetailService;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailService customUserDetailService) {
        super(authenticationManager);
        this.customUserDetailService = customUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        Cookie authCookie = null;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token".equals(cookie.getName()) && !StringUtils.isEmpty(cookie.getValue())) {
                    authCookie = cookie;
                    break;
                }
            }
        }

        if (authCookie == null || StringUtils.isEmpty(authCookie.getValue())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            final var authenticationToken = getAuthenticationToken(authCookie);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            chain.doFilter(request, response);
        } catch (ExpiredJwtException ex) {
            response.setStatus(401);
            response.addCookie(clearAuthCookie());
        }
    }

    public static Cookie clearAuthCookie() {
        final var cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        log.info("user not logged");
        return cookie;
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(Cookie authCookie) {
        if (authCookie.getValue() == null || StringUtils.isEmpty(authCookie.getValue())) {
            return null;
        }

        final var username = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(authCookie.getValue().replace(TOKEN_PREFIX, "")).getBody().getSubject();
        final var userDetails = customUserDetailService.loadUserByUsername(username);
        return username != null ? new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities()) : null;
    }

}
