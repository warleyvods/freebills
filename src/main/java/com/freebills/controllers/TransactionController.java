package com.freebills.controllers;


import com.freebills.controllers.dtos.requests.TransactionPostRequestDTO;
import com.freebills.controllers.dtos.requests.TransactionPutRequesDTO;
import com.freebills.controllers.dtos.responses.TransactionResponseDTO;
import com.freebills.controllers.mappers.TransactionMapper;
import com.freebills.domains.enums.TransactionType;
import com.freebills.usecases.CreateTransaction;
import com.freebills.usecases.DeleteTransaction;
import com.freebills.usecases.FindTransaction;
import com.freebills.usecases.UpdateTransaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionMapper mapper;
    private final CreateTransaction createTransaction;
    private final UpdateTransaction updateTransaction;
    private final FindTransaction findTransaction;
    private final DeleteTransaction deleteTransaction;

    @ResponseStatus(CREATED)
    @PostMapping
    public TransactionResponseDTO save(@RequestBody @Valid final TransactionPostRequestDTO transactionPostRequestDto) {
        final var transaction = mapper.toDomain(transactionPostRequestDto);
        return mapper.fromDomain(createTransaction.execute(transaction));
    }

    @ResponseStatus(OK)
    @GetMapping("/filter")
    public Page<TransactionResponseDTO> byUserDateFilter(
            final Principal principal,
            @RequestParam(required = false) final Integer month,
            @RequestParam(required = false) final Integer year,
            @RequestParam(required = false) final String keyword,
            @RequestParam(required = false) final String transactionType,
            final Pageable pageable) {
        return findTransaction.findAllByUserDateFilter(
                        principal.getName(), month, year, pageable, keyword,
                        transactionType != null ? TransactionType.valueOf(transactionType) : null)
                .map(mapper::fromDomain);
    }

    @ResponseStatus(OK)
    @GetMapping("{id}")
    public TransactionResponseDTO findTransactionById(@PathVariable final Long id) {
        return mapper.fromDomain(findTransaction.findById(id));
    }

    @ResponseStatus(OK)
    @PutMapping
    public TransactionResponseDTO update(@RequestBody @Valid final TransactionPutRequesDTO transactionPutRequesDTO) {
        final var transactionFinded = findTransaction.findById(transactionPutRequesDTO.id());
        final var update = updateTransaction.execute(mapper.updateTransactionFromDto(transactionPutRequesDTO, transactionFinded));
        return mapper.fromDomain(update);
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("{id}")
    public void deleteById(@PathVariable final Long id) {
        deleteTransaction.delete(id);
    }
}
