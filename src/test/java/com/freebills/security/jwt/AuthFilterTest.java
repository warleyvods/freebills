package com.freebills.security.jwt;

import com.freebills.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.ArrayList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthFilterTest {

    @Mock
    private TokenUtils tokenUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthFilter authFilter;

    @BeforeEach
    void setUp() {
        when(tokenUtils.getTokenFromCookie(request)).thenReturn("token");
        when(tokenUtils.validateToken("token")).thenReturn(true);
        when(tokenUtils.getUserName("token")).thenReturn("username");
    }

    @Test
    void shouldAuthenticateWhenTokenIsValidAndUserIsEnabled() throws IOException, ServletException {
        when(tokenUtils.getTokenFromCookie(request)).thenReturn("token");
        when(tokenUtils.validateToken("token")).thenReturn(true);
        when(tokenUtils.getUserName("token")).thenReturn("username");

        UserDetails userDetails = new User("username", "password", new ArrayList<>());
        when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);

        authFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}