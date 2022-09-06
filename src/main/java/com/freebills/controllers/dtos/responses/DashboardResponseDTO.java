package com.freebills.controllers.dtos.responses;

import java.math.BigDecimal;

public record DashboardResponseDTO(
        BigDecimal totalBalance,
        BigDecimal totalRevenue,
        BigDecimal totalExpensive,
        BigDecimal totalExpensiveCards
) {
}
