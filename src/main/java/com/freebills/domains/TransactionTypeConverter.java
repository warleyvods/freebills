package com.freebills.domains;

import com.freebills.domains.enums.TransactionType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TransactionTypeConverter implements AttributeConverter<TransactionType, String> {
 
    @Override
    public String convertToDatabaseColumn(TransactionType transactionType) {
        if (transactionType == null) {
            return null;
        }

        return switch (transactionType) {
            case REVENUE -> "REVENUE";
            case EXPENSE -> "EXPENSE";
        };
    }

    @Override
    public TransactionType convertToEntityAttribute(String transactionType) {
        if (transactionType == null) {
            return null;
        }

        return switch (transactionType) {
            case "REVENUE" -> TransactionType.REVENUE;
            case "EXPENSE" -> TransactionType.EXPENSE;
            default -> throw new IllegalArgumentException(transactionType + " not supported.");
        };
    }
}