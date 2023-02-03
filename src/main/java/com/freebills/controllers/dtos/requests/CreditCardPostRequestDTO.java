package com.freebills.controllers.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreditCardPostRequestDTO(
        @NotNull
        Long accountId,

        @NotNull
        BigDecimal cardLimit,

        @NotBlank
        String description,

        @NotBlank
        String cardFlag,

        @NotNull
        Integer expirationDay,

        @NotNull
        Integer closingDay
) {
}
