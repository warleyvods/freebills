package com.freebills.controllers.dtos.responses;

import java.util.List;

public record DashboardGraphResponseDTO(
        List<String> labels,
        List<Double> series
) {
}
