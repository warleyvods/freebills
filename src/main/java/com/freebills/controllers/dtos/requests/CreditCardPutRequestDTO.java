package com.freebills.controllers.dtos.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public record CreditCardPutRequestDTO(

        @NotNull
        Long id,

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
