package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.CCTransactionPostRequestDTO;
import com.freebills.controllers.dtos.responses.CCTransactionResponseDTO;
import com.freebills.controllers.mappers.CCTransactionMapper;
import com.freebills.usecases.CreateCCTransaction;
import com.freebills.usecases.FindCCTransaction;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "CC Transaction Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cc-transaction")
public class CCTransactionController {

    private final CCTransactionMapper ccTransactionMapper;
    private final CreateCCTransaction createCCTransaction;
    private final FindCCTransaction findCCTransaction;

    @ResponseStatus(ACCEPTED)
    @PostMapping
    public CCTransactionResponseDTO save(@RequestBody @Valid CCTransactionPostRequestDTO request) {
        return ccTransactionMapper.toDTO(createCCTransaction.execute(ccTransactionMapper.toDomain(request)));
    }

    @ResponseStatus(OK)
    @GetMapping("/card/{id}")
    public Page<CCTransactionResponseDTO> listAll(final Principal principal,
                                                  @PathVariable(value = "id") final Long cardId,
                                                  @RequestParam(required = false) final Integer month,
                                                  @RequestParam(required = false) final Integer year,
                                                  @RequestParam(required = false) final String keyword,
                                                  Pageable pageable) {
        return findCCTransaction.findAllWithFilters(cardId, principal.getName(), month, year, pageable, keyword)
                .map(ccTransactionMapper::toDTO);
    }

    @ResponseStatus(OK)
    @GetMapping("/transaction/{id}")
    public CCTransactionResponseDTO findById(final Principal principal, @PathVariable final Long id) {
        return ccTransactionMapper.toDTO(findCCTransaction.findById(id, principal.getName()));
    }



}
