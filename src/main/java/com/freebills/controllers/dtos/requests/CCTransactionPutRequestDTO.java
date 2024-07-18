package com.freebills.controllers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.freebills.gateways.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CCTransactionPutRequestDTO(
        Long id,
        BigDecimal amount,
        @JsonFormat(pattern="dd-MM-yyyy")
        LocalDate date,
        String description,
        Long categoryId,
        Long creditCardId,
        @JsonFormat(pattern="dd-MM-yyyy")
        LocalDate expirationDate
) { }
