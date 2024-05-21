package com.freebills.controllers;


import com.freebills.controllers.dtos.requests.LoginRequestDTO;
import com.freebills.controllers.dtos.requests.SignupUserRequestDTO;
import com.freebills.controllers.dtos.responses.MessageResponse;
import com.freebills.controllers.mappers.UserMapper;
import com.freebills.security.services.LoginService;
import com.freebills.usecases.CreateUser;
import com.freebills.usecases.FindUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.Boolean.TRUE;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final FindUser findUser;
    private final CreateUser createUser;
    private final UserMapper userMapper;
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequest) {
        final var responseCookie = loginService.validateLogin(loginRequest);
        return ok().header(SET_COOKIE, responseCookie.toString()).body(null);
    }

    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logoutUser() {
        ResponseCookie cookie = loginService.logout();
        return ok().header(SET_COOKIE, cookie.toString()).body(new MessageResponse("You've been signed out!"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupUserRequestDTO signUpRequestDTO) {
        if (TRUE.equals(findUser.existsByLogin(signUpRequestDTO.login()))) {
            return badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (TRUE.equals(findUser.existsByEmail(signUpRequestDTO.email()))) {
            return badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        createUser.create(userMapper.toDomainUser(signUpRequestDTO));
        return ResponseEntity.status(CREATED).build();
    }
}
