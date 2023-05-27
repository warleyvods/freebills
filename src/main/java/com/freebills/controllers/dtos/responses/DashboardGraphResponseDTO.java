package com.freebills.controllers.dtos.responses;

import java.math.BigDecimal;
import java.util.List;

public record DashboardGraphResponseDTO(
        List<String> labels,
        List<BigDecimal> series
) {
}
