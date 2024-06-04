package com.freebills.controllers;


import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequestDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.controllers.mappers.TransactionMapper;
import com.freebills.gateways.entities.enums.TransactionType;
import com.freebills.usecases.CreateTransaction;
import com.freebills.usecases.DeleteTransaction;
import com.freebills.usecases.DuplicateTransaction;
import com.freebills.usecases.FindTransaction;
import com.freebills.usecases.UpdateTransaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionMapper transactionMapper;
    private final CreateTransaction createTransaction;
    private final UpdateTransaction updateTransaction;
    private final FindTransaction findTransaction;
    private final DeleteTransaction deleteTransaction;
    private final DuplicateTransaction duplicateTransaction;

    @ResponseStatus(CREATED)
    @PostMapping
    public TransactionResponseDTO save(@RequestBody @Valid final TransactionPostRequestDTO transactionPostRequestDto) {
        final var transaction = transactionMapper.toDomain(transactionPostRequestDto);
        return transactionMapper.fromDomain(createTransaction.execute(transaction));
    }

    @ResponseStatus(OK)
    @GetMapping("/filter")
    public Page<TransactionResponseDTO> findAllWithFilters(
            final Principal principal,
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            @RequestParam(required = false) final String keyword,
            @RequestParam(required = false) final String transactionType,
            final Pageable pageable) {

        return findTransaction.findAllWithFilters(principal.getName(), month, year, pageable, keyword, transactionType != null ? TransactionType.valueOf(transactionType) : null)
                .map(transactionMapper::fromDomain);
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    public TransactionResponseDTO findTransactionById(@PathVariable final Long id) {
        return transactionMapper.fromDomain(findTransaction.findById(id));
    }

    @ResponseStatus(OK)
    @PutMapping
    public TransactionResponseDTO update(@RequestBody @Valid final TransactionPutRequestDTO transactionPutRequestDTO) {
        final var transactionFinded = findTransaction.findById(transactionPutRequestDTO.id());
        final var update = updateTransaction.execute(transactionMapper.updateTransaction(transactionPutRequestDTO, transactionFinded));
        return transactionMapper.fromDomain(update);
    }

    @ResponseStatus(OK)
    @PutMapping(value = "/duplicate")
    public TransactionResponseDTO duplicate(@RequestParam final Long id) {
        final var execute = duplicateTransaction.execute(id);
        return transactionMapper.fromDomain(execute);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable final Long id) {
        deleteTransaction.delete(id);
    }
}
