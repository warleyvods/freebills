package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.gateways.AccountGateway;
import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class TransactionValidation {

    private final AccountGateway accountGateway;

    public void transactionCreationValidation(Transaction transaction) {

    }

    public void transactionUpdateValidation(Transaction transaction) {

    }
}
