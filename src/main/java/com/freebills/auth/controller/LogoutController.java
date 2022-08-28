package com.freebills.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Logout Controller")
@RequiredArgsConstructor
@RequestMapping("v1/logout")
@RestController
public class LogoutController {

    @ResponseStatus(OK)
    @GetMapping
    public void logout(HttpServletResponse response) {
        final var cookie = new Cookie("token", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(".wavods.com");

        response.addCookie(cookie);
    }
}
