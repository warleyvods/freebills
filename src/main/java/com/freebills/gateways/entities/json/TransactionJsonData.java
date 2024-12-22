package com.freebills.gateways.entities.json;

import com.freebills.domain.Account;
import com.freebills.domain.Category;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionJsonData(
        Long id,
        LocalDate date,
        String description,
        String barCode,
        Boolean bankSlip,
        TransactionType transactionType,
        TransactionCategory transactionCategory,
        Boolean paid,
        Account account,
        Category category,
        LocalDateTime createdAt,
        BigDecimal amount) implements Serializable { }
