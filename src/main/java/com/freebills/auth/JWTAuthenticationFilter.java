package com.freebills.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freebills.auth.dtos.LoginRequestDTO;
import com.freebills.repositories.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.naming.NoPermissionException;
import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import static com.freebills.auth.SecurityConstants.EXPIRATION_TIME;
import static com.freebills.auth.SecurityConstants.SECRET;


@Slf4j
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            final var user = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDTO.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.login(), user.password()));
        } catch (IOException e) {
            log.info("[attemptAuthentication][error]:" + e.getMessage());
            return null;
        }
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        final var login = (((User) authResult.getPrincipal()).getUsername());
        final var user = userRepository.findByLogin(login);

        if (!user.isActive()) {
            throw new NoPermissionException("User " + user.getLogin() + " are disabled!");
        }

        final var authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : authResult.getAuthorities()) {
            final var authority = grantedAuthority.getAuthority();
            authorities.add(authority);
        }


        final var token = Jwts.builder()
                .claim("id", user.getId())
                .setSubject(login)
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("authorities", authorities)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        final var cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(EXPIRATION_TIME.intValue());
        cookie.setPath("/");
//        cookie.setDomain(".wavods.com");

        response.addHeader("Access-Control-Expose-Headers", "Authorization");
        response.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");
        response.addCookie(cookie);
    }

}
