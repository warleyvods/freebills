package com.freebills.security.services;

import com.freebills.domain.User;
import com.freebills.security.jwt.TokenUtils;
import com.freebills.usecases.CreateUser;
import com.freebills.usecases.FindUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.freebills.gateways.entities.Source.GITHUB;
import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${front.url}")
    private String frontendUrl;

    private final CreateUser createUser;
    private final FindUser findUser;
    private final TokenUtils tokenUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals("github")) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();
            String email = attributes.getOrDefault("email", "").toString();
            String name = attributes.getOrDefault("name", "").toString();
            String login = attributes.getOrDefault("login", "").toString();

            //BUG de vulnerabilidade
            User user = findOrCreateUser(email, name, login);

            authenticateUser(user, attributes, oAuth2AuthenticationToken);
            addJwtTokenToResponse(response, user);
        }

        this.setAlwaysUseDefaultTargetUrl(true);
        this.setDefaultTargetUrl(frontendUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }

    private User findOrCreateUser(String email, String name, String login) {
        if (Boolean.TRUE.equals(findUser.existsByEmail(email))) {
            return findUser.byLoginOrEmail(email);
        } else {
            return createUser.execute(buildNewUserBySocialLogin(email, name, login));
        }
    }

    private User buildNewUserBySocialLogin(String email, String name, String login) {
        User user = new User();
        user.setLogin(login);
        user.setName(name);
        user.setEmail(email);
        user.setSource(GITHUB);
        user.setPassword(new BCryptPasswordEncoder().encode(now().toString()));
        user.setActive(true);
        user.setAdmin(false);
        return user;
    }

    private void authenticateUser(User user, Map<String, Object> attributes, OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (Boolean.TRUE.equals(user.getAdmin())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        DefaultOAuth2User newUser = new DefaultOAuth2User(authorities, attributes, "id");
        Authentication securityAuth = new OAuth2AuthenticationToken(newUser, authorities, oAuth2AuthenticationToken.getAuthorizedClientRegistrationId());
        SecurityContextHolder.getContext().setAuthentication(securityAuth);
    }

    private void addJwtTokenToResponse(HttpServletResponse response, User user) {
        var jwtToken = tokenUtils.generateToken(user);
        response.addHeader(HttpHeaders.SET_COOKIE, jwtToken.toString());
    }
}
