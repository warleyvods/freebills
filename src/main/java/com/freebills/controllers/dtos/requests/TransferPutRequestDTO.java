package com.freebills.controllers.dtos.requests;

import java.time.LocalDateTime;

public record TransferPutRequestDTO(
        Long id,
        String observation,
        String description,
        String transferCategory,
        Long from,
        Long to,
        LocalDateTime createdAt
) {
}
