package com.freebills.controllers;


import com.freebills.controllers.dtos.requests.AccountPatchArchivedRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPutRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.controllers.mappers.AccountMapper;
import com.freebills.usecases.CreateAccount;
import com.freebills.usecases.DeleteAccount;
import com.freebills.usecases.FindAccount;
import com.freebills.usecases.UpdateAccount;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "Account Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/accounts")
public class AccountController {

    private final AccountMapper mapper;
    private final FindAccount findAccount;
    private final UpdateAccount updateAccount;
    private final CreateAccount createAccount;
    private final DeleteAccount deleteAccount;

    @ResponseStatus(CREATED)
    @PostMapping
    public AccountResponseDTO save(@RequestBody @Valid final AccountPostRequestDTO accountPostRequestDTO) {
        final var account = mapper.toDomain(accountPostRequestDTO);
        return mapper.fromDomain(createAccount.create(account));
    }

    @ResponseStatus(OK)
    @GetMapping
    public List<AccountResponseDTO> findAllAccountsNonArchived(Principal principal) {
        return mapper.fromDomainList(findAccount.findByAccountsNonArchived(principal.getName()));
    }

    @ResponseStatus(OK)
    @GetMapping("/archived")
    public List<AccountResponseDTO> findAllAccountsArchived(Principal principal) {
        return mapper.fromDomainList(findAccount.findByAccountsArchived(principal.getName()));
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    public AccountResponseDTO findById(@PathVariable final Long id) {
        return mapper.fromDomain(findAccount.byId(id));
    }

    @ResponseStatus(OK)
    @PutMapping
    public AccountResponseDTO update(@RequestBody @Valid final AccountPutRequestDTO accountPutRequestDTO) {
        final var accountFinded = findAccount.byId(accountPutRequestDTO.accountId());
        final var toJson = updateAccount.update(mapper.updateAccountFromDTO(accountPutRequestDTO, accountFinded));
        return mapper.fromDomain(toJson);
    }

    @ResponseStatus(OK)
    @PatchMapping
    public AccountResponseDTO updateArchiveAcc(@RequestBody @Valid final AccountPatchArchivedRequestDTO accountDTO) {
        final var accountFinded = findAccount.byId(accountDTO.id());
        final var toJson = updateAccount.update(mapper.updateArchiveAccountFromDTO(accountDTO, accountFinded));
        return mapper.fromDomain(toJson);
    }

    @DeleteMapping("{accountId}")
    public void deleteAccount(@PathVariable final Long accountId) {
        deleteAccount.deleteAccount(accountId);
    }
}
