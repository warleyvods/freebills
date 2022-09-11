package com.freebills.controllers.dtos.responses;


public record AccountResponseDTO(
        Long id,
        Double amount,
        String description,
        String accountType,
        boolean dashboard,
        boolean archived,
        String bankType
) {
}
