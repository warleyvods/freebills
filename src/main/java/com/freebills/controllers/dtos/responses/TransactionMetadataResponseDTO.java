package com.freebills.controllers.dtos.responses;

public record TransactionMetadataResponseDTO(
        boolean bankSlip,
        boolean observation
) {
}
