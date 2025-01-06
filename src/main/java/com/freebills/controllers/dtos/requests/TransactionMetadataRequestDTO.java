package com.freebills.controllers.dtos.requests;

public record TransactionMetadataRequestDTO(
        boolean bankSlip,
        boolean observation
) {
}
