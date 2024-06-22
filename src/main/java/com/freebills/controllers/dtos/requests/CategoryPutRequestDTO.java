package com.freebills.controllers.dtos.requests;

public record CategoryPutRequestDTO(
        Long id,
        String name,
        String color
) {
}
