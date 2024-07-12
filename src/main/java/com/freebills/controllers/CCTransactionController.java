package com.freebills.controllers;

import com.freebills.controllers.dtos.requests.CCTransactionPostRequestDTO;
import com.freebills.controllers.dtos.responses.CCTransactionResponseDTO;
import com.freebills.controllers.mappers.CCTransactionMapper;
import com.freebills.usecases.CreateCCTransaction;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.ACCEPTED;

@Tag(name = "CC Transaction Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/cc-transaction")
public class CCTransactionController {

    private final CCTransactionMapper ccTransactionMapper;
    private final CreateCCTransaction createCCTransaction;

    @ResponseStatus(ACCEPTED)
    @PostMapping
    public CCTransactionResponseDTO save(@RequestBody @Valid CCTransactionPostRequestDTO request) {
        return null;
    }
}
