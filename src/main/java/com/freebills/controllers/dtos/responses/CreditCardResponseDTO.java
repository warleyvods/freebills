package com.freebills.controllers.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreditCardResponseDTO(
        Long id,
        BigDecimal cardLimit,
        String description,
        String cardFlag,
        Integer expirationDay,
        Integer closingDay,
        Boolean archived,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
