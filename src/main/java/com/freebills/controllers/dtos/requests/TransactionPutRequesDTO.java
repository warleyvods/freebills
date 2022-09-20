package com.freebills.controllers.dtos.requests;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record TransactionPutRequesDTO(

        @NotNull
        Long id,

        @JsonFormat(pattern = "")
        @NotNull
        Double amount,

        boolean paid,

        @NotBlank
        String description,

        @JsonFormat(pattern="dd-MM-yyyy")
        @NotNull
        LocalDate date,

        @NotBlank
        String transactionType,

        @NotBlank
        String transactionCategory,

        boolean bankSlip,

        String barCode
) {
}
