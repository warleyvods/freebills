package com.freebills.gateways.entities.json;

import com.freebills.gateways.entities.enums.TransferType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferJsonData(
        Long id,
        BigDecimal amount,
        String observation,
        String description,
        TransferType transferType,
        Long fromAccountId,
        Long toAccountId,
        LocalDateTime updatedAt,
        LocalDateTime createdAt) implements Serializable {

}
