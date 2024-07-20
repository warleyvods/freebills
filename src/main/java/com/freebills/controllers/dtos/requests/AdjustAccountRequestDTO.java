package com.freebills.controllers.dtos.requests;

import java.math.BigDecimal;

public record AdjustAccountRequestDTO(
        Long accountId,
        BigDecimal amount,
        String type
) {
}
