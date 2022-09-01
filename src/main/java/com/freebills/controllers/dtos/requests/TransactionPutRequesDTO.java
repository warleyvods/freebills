package com.freebills.controllers.dtos.requests;

import java.time.LocalDate;

public record TransactionPutRequesDTO(

        Long id,
        Long accountId,
        Double amount,
        boolean paid,
        String description,
        LocalDate date,
        String transactionType,
        String transactionCategory

) {
}
