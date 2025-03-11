package com.freebills.domain;

import com.freebills.gateways.entities.enums.TransactionCategory;
import com.freebills.gateways.entities.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString(exclude = {"account", "category"})
@RequiredArgsConstructor
public class Transaction implements Serializable {

    private Long id;
    private LocalDate date;
    private String description;
    private String barCode;
    private TransactionType transactionType;
    private TransactionCategory transactionCategory;
    private Account account;
    private Category category;
    private LocalDateTime createdAt;

    @NonNull
    private BigDecimal amount;

    private String observation;
    
    private FileReference receipt;
    
    private TransactionMetadata metadata;
    
    // Métodos de conveniência para acessar metadados
    public boolean isPaid() {
        return metadata != null && Boolean.TRUE.equals(metadata.getHasPaidConfirmation());
    }
    
    public boolean isBankSlip() {
        return metadata != null && Boolean.TRUE.equals(metadata.getIsBankSlip());
    }
    
    public void setPaid(boolean paid) {
        if (metadata == null) {
            metadata = new TransactionMetadata();
        }
        metadata.setHasPaidConfirmation(paid);
    }
    
    public void setBankSlip(boolean bankSlip) {
        if (metadata == null) {
            metadata = new TransactionMetadata();
        }
        metadata.setIsBankSlip(bankSlip);
    }
}
