package com.freebills.security.jwt;

import com.freebills.domain.User;
import com.freebills.security.services.UserDetailsImpl;
import com.freebills.utils.TestContainerBase;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
class TokenUtilsIntegrationTest extends TestContainerBase {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private Environment environment;

    @Value("${api.app.jwtSecret}")
    private String jwtSecret;

    @Value("${api.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private UserDetailsImpl userDetails;
    private User user;

    @BeforeEach
    void setUp() {
        userDetails = new UserDetailsImpl(1L, "user", "user@test.com", "password", List.of());
        user = new User();
    }

    @Test
    void generateTokenAndExtractInformation() {
        String token = tokenUtils.generateTokenFromUserDetails(userDetails);

        assertNotNull(token);
        assertEquals(userDetails.getUsername(), tokenUtils.getUserName(token));
    }

    @Test
    void getTokenFromCookie() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        Cookie cookie = new Cookie("token", "test_token");
        request.setCookies(cookie);

        String token = tokenUtils.getTokenFromCookie(request);

        assertEquals("test_token", token);
    }

    @Test
    void generateTokenAndCheckCookie() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        var cookie = tokenUtils.generateToken(user);

        assertNotNull(cookie);
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertTrue(cookie.isSecure());
    }

    @Test
    void cleanToken() {
        MockHttpServletResponse response = new MockHttpServletResponse();
        var cookie = tokenUtils.cleanToken();

        assertNotNull(cookie);
        assertEquals("/", cookie.getPath());
    }
}
