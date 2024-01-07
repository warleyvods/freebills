package com.freebills.controllers;


import com.freebills.controllers.dtos.requests.LoginRequestDTO;
import com.freebills.controllers.dtos.requests.SignupUserRequestDTO;
import com.freebills.controllers.dtos.responses.MessageResponse;
import com.freebills.controllers.mappers.UserMapper;
import com.freebills.security.jwt.JWTUtils;
import com.freebills.security.services.UserDetailsImpl;
import com.freebills.usecases.CreateUser;
import com.freebills.usecases.FindUser;
import com.freebills.usecases.UpdateUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final FindUser findUser;
    private final UpdateUser updateUser;
    private final CreateUser createUser;
    private final JWTUtils jwtUtils;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.login(), loginRequestDTO.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        var jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        lastAccess(authentication);
        return ResponseEntity.ok().header(SET_COOKIE, jwtCookie.toString()).body(null);
    }

    private void lastAccess(Authentication authentication) {
        final String username = ((UserDetailsImpl) authentication.getPrincipal()).username();
        final var user = findUser.byLogin(username);
        user.setLastAccess();
        updateUser.update(user);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupUserRequestDTO signUpRequestDTO) {
        if (Boolean.TRUE.equals(findUser.existsByLogin(signUpRequestDTO.login()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (Boolean.TRUE.equals(findUser.existsByEmail(signUpRequestDTO.email()))) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        final var userEntity = userMapper.toDomainUser(signUpRequestDTO);
        return ResponseEntity.ok(createUser.create(userEntity));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(SET_COOKIE, cookie.toString()).body(new MessageResponse("You've been signed out!"));
    }
}
