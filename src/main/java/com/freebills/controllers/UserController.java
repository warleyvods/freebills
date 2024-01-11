package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.SignupUserRequestDTO;
import com.freebills.controllers.dtos.requests.UserPostRequestDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "User Controller")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RequestMapping("v1/user")
@RestController
public class UserController {

    private final CreateUser createUser;
    private final FindUser findUser;
    private final DeleteUser deleteUser;
    private final UserMapper userMapper;
    private final UpdateUser updateUser;

    @ResponseStatus(CREATED)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO save(@RequestBody @Valid final UserPostRequestDTO userPostRequestDTO) {
        final var user = createUser.create(userMapper.toDomain(userPostRequestDTO));
        return userMapper.fromDomain(user);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/public/save")
    public UserResponseDTO savePublicUser(@RequestBody @Valid final SignupUserRequestDTO signupUserRequestDTO) {
        final var user = createUser.create(userMapper.toDomainUser(signupUserRequestDTO));
        return userMapper.fromDomain(user);
    }

    @ResponseStatus(OK)
    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public UserResponseDTO update(@RequestBody @Valid final UserPutRequestDTO userPutRequestDTO, Principal principal) {
        final var userFinded = findUser.byLogin(principal.getName());
        final var toJson = updateUser.update(userMapper.updateUserFromDTO(userPutRequestDTO, userFinded));
        return userMapper.fromDomain(toJson);
    }

    @ResponseStatus(OK)
    @PatchMapping
    @PreAuthorize("hasRole('USER')")
    public void updatePassword(@RequestBody @Valid final UserPutPasswordRequestDTO userPassword, Principal principal) {
        final var userFinded = findUser.byLogin(principal.getName());
        updateUser.updatePassword(userMapper.updatePasswordFromDTO(userPassword, userFinded));
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO findById(@PathVariable final Long id) {
        final var user = findUser.byId(id);
        return userMapper.fromDomain(user);
    }

    @ResponseStatus(OK)
    @GetMapping("login/{login}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO findByLogin(@PathVariable final String login) {
        final var user = findUser.byLogin(login);
        return userMapper.fromDomain(user);
    }


    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(@PathVariable final Long id) {
        deleteUser.byId(id);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteByIds(@RequestParam final List<Long> ids) {
        deleteUser.byIds(ids);
    }

    @ResponseStatus(OK)
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return findUser.all(pageable).map(userMapper::fromDomain);
    }
}
