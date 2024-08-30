package com.freebills.controllers;


import com.freebills.controllers.dtos.requests.AccountPatchArchivedRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPostRequestDTO;
import com.freebills.controllers.dtos.requests.AccountPutRequestDTO;
import com.freebills.controllers.dtos.requests.AdjustAccountRequestDTO;
import com.freebills.controllers.dtos.responses.AccountResponseDTO;
import com.freebills.controllers.mappers.AccountMapper;
import com.freebills.usecases.AdjustAccount;
import com.freebills.usecases.CreateAccount;
import com.freebills.usecases.DeleteAccount;
import com.freebills.usecases.FindAccount;
import com.freebills.usecases.UpdateAccount;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
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
    private final AdjustAccount adjustAccount;

    @ResponseStatus(CREATED)
    @PostMapping
    public AccountResponseDTO save(@RequestBody @Valid final AccountPostRequestDTO accountPostRequestDTO, Principal principal) {
        final var account = mapper.toDomain(accountPostRequestDTO);
        return mapper.toDTO(createAccount.create(account, principal.getName()));
    }

    @ResponseStatus(OK)
    @GetMapping
    public List<AccountResponseDTO> findAllAccountsNonArchived(Principal principal) {
        return findAccount.findByAccountsNonArchived(principal.getName()).stream().map(mapper::toDTO).toList();
    }

    @ResponseStatus(OK)
    @GetMapping("/archived")
    public List<AccountResponseDTO> findAllAccountsArchived(Principal principal) {
        return findAccount.findByAccountsArchived(principal.getName()).stream().map(mapper::toDTO).toList();
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    public AccountResponseDTO findById(@PathVariable final Long id, Principal principal) {
        final var account = mapper.toDTO(findAccount.byId(id));
        log.info("account: {} finded by {}", account.id(), principal.getName());
        return account;
    }

    @ResponseStatus(OK)
    @PutMapping
    public AccountResponseDTO update(@RequestBody @Valid final AccountPutRequestDTO accountPutRequestDTO) {
        final var accountFinded = findAccount.byId(accountPutRequestDTO.accountId());
        final var toJson = updateAccount.update(mapper.updateAccountFromDTO(accountPutRequestDTO, accountFinded));
        return mapper.toDTO(toJson);
    }

    @ResponseStatus(OK)
    @PatchMapping
    public AccountResponseDTO updateArchiveAcc(@RequestBody @Valid final AccountPatchArchivedRequestDTO accountDTO) {
        final var accountFinded = findAccount.byId(accountDTO.id());
        final var toJson = updateAccount.update(mapper.updateArchiveAccountFromDTO(accountDTO, accountFinded));
        return mapper.toDTO(toJson);
    }

    @ResponseStatus(OK)
    @PatchMapping("/readjustment")
    public void adjustAccount(@RequestBody @Valid final AdjustAccountRequestDTO request, final Principal principal) {
        adjustAccount.execute(request.accountId(), request.amount(), principal.getName());
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{accountId}")
    public void deleteAccount(@PathVariable final Long accountId) {
        deleteAccount.deleteAccount(accountId);
    }
}
