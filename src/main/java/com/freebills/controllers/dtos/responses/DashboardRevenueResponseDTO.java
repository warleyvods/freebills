package com.freebills.controllers.dtos.responses;

import java.math.BigDecimal;

public record DashboardRevenueResponseDTO(
        BigDecimal totalBalance,
        BigDecimal totalRevenuePending,
        BigDecimal totalRevenueReceived,
        BigDecimal totalRevenue
) {
}
