package com.freebills.controllers.dtos.responses;

import java.math.BigDecimal;

public record DashboardExpenseResponseDTO(
        BigDecimal totalBalance,
        BigDecimal totalExpensePending,
        BigDecimal totalExpenseReceived,
        BigDecimal totalExpense
) {
}
