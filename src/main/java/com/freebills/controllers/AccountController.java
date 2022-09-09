package com.freebills.controllers;


import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPutRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.controllers.mappers.AccountMapper;
import com.freebills.usecases.CreateAccount;
import com.freebills.usecases.FindAccount;
import com.freebills.usecases.UpdateAccount;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Tag(name = "Account Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/accounts")
public class AccountController {

    private final AccountMapper mapper;
    private final FindAccount findAccount;
    private final UpdateAccount updateAccount;
    private final CreateAccount createAccount;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccountResponseDTO save(@RequestBody @Valid final AccountPostRequestDTO accountPostRequestDTO) {
        final var account = mapper.toDomain(accountPostRequestDTO);
        return mapper.fromDomain(createAccount.create(account));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<AccountResponseDTO> findAll(Principal principal) {
        return mapper.fromDomainList(findAccount.findByUserId(principal.getName()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    public AccountResponseDTO update(@RequestBody @Valid final AccountPutRequestDTO accountPutRequestDTO) {
        final var accountFinded = findAccount.byId(accountPutRequestDTO.id());
        final var toJson = updateAccount.update(mapper.updateAccountFromDto(accountPutRequestDTO, accountFinded));
        return mapper.fromDomain(toJson);
    }
}
