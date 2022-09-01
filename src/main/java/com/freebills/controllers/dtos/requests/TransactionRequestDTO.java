package com.freebills.controllers.dtos.requests;

import java.time.LocalDate;

public record TransactionRequestDTO(

        Long accountId,

        Double amount,

        boolean paid,

        String description,

        LocalDate date,

        String transactionType,

        String transactionCategory

) {
}
