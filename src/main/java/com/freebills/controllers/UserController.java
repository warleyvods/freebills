package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.UserPostRequestDTO;
import com.freebills.controllers.dtos.requests.SignupUserRequestDTO;
import com.freebills.controllers.dtos.requests.UserPutPasswordRequestDTO;
import com.freebills.controllers.dtos.requests.UserPutRequestDTO;
import com.freebills.controllers.dtos.responses.UserResponseDTO;
import com.freebills.controllers.mappers.UserMapper;
import com.freebills.usecases.CreateUser;
import com.freebills.usecases.DeleteUser;
import com.freebills.usecases.FindUser;
import com.freebills.usecases.UpdateUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.*;

@Tag(name = "User Controller")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("v1/user")
@RestController
public class UserController {

    private final CreateUser createUser;
    private final FindUser findUser;
    private final DeleteUser deleteUser;
    private final UserMapper mapper;
    private final UpdateUser updateUser;

    @ResponseStatus(CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO save(@RequestBody @Valid final UserPostRequestDTO userPostRequestDTO) {
        final var user = createUser.create(mapper.toDomain(userPostRequestDTO));
        return mapper.fromDomain(user);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/public/save")
    public UserResponseDTO savePublicUser(@RequestBody @Valid final SignupUserRequestDTO signupUserRequestDTO) {
        final var user = createUser.create(mapper.toDomainUser(signupUserRequestDTO));
        return mapper.fromDomain(user);
    }

    @ResponseStatus(OK)
    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public UserResponseDTO update(@RequestBody @Valid final UserPutRequestDTO userPutRequestDTO) {
        final var userFinded = findUser.byId(userPutRequestDTO.id());
        final var toJson = updateUser.update(mapper.updateUserFromDTO(userPutRequestDTO, userFinded));
        return mapper.fromDomain(toJson);
    }

    @ResponseStatus(OK)
    @PatchMapping
    @PreAuthorize("hasRole('USER')")
    public void updatePassword(@RequestBody @Valid final UserPutPasswordRequestDTO userPassword) {
        final var userFinded = findUser.byId(userPassword.id());
        updateUser.updatePassword(mapper.updatePasswordFromDTO(userPassword, userFinded));
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO findById(@PathVariable final Long id) {
        final var user = findUser.byId(id);
        return mapper.fromDomain(user);
    }

    @ResponseStatus(OK)
    @GetMapping("login/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO findByLogin(@PathVariable final String login) {
        final var user = findUser.byLogin(login);
        return mapper.fromDomain(user);
    }


    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable final Long id) {
        deleteUser.byId(id);
    }

    @ResponseStatus(OK)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return findUser.all(pageable).map(mapper::fromDomain);
    }
}
