package com.freebills.controllers;

import com.freebills.controllers.dtos.responses.UserResponseDTO;
import com.freebills.controllers.mappers.UserMapper;
import com.freebills.gateways.UserGateway;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Me Controller")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("v1/me")
@RestController
public class MeController {

    private final UserGateway userGateway;
    private final UserMapper userMapper;

    @ResponseStatus(OK)
    @GetMapping
    public UserResponseDTO findById(Principal principal) {
        return userMapper.fromDomain(userGateway.findByLogin(principal.getName()));
    }
}
