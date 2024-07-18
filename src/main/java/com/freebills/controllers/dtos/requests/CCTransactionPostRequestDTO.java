package com.freebills.controllers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CCTransactionPostRequestDTO(
        BigDecimal amount,
        @JsonFormat(pattern="dd-MM-yyyy")
        LocalDate date,
        String description,
        Long categoryId,
        Long creditCardId,
        @JsonFormat(pattern="dd-MM-yyyy")
        LocalDate expirationDate
) { }
