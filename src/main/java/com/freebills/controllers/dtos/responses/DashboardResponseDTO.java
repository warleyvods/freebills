package com.freebills.controllers.dtos.responses;

import java.math.BigDecimal;

public record DashboardResponseDTO(
        String type,
        BigDecimal balanceType
) {
}
