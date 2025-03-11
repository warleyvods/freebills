package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.domain.TransactionMetadata;
import com.freebills.events.transaction.TransactionCreatedEvent;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.TransactionMetadataGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateTransaction {

    private final TransactionGateway transactionGateway;
    private final TransactionMetadataGateway metadataGateway;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Transaction execute(final Transaction transaction) {
        // Se recebemos metadados no DTO, usamos
        TransactionMetadata metadata = transaction.getMetadata();
        
        // Se não recebemos metadados, criamos um novo
        if (metadata == null) {
            metadata = TransactionMetadata.builder().build();
        }
        
        // Atualizamos os campos derivados
        metadata.setHasReceipt(transaction.getReceipt() != null);
        metadata.setHasObservation(transaction.getObservation() != null && !transaction.getObservation().isEmpty());
        
        // Se não foram especificados, inicializamos com valores padrão
        if (metadata.getIsRecurring() == null) metadata.setIsRecurring(false);
        if (metadata.getIsFixed() == null) metadata.setIsFixed(false);
        if (metadata.getIsCreditCardPayment() == null) metadata.setIsCreditCardPayment(false);
        if (metadata.getIsBankSlip() == null) metadata.setIsBankSlip(false);
        if (metadata.getHasPaidConfirmation() == null) metadata.setHasPaidConfirmation(false);
        if (metadata.getTags() == null) metadata.setTags("");
        if (metadata.getIsFavorite() == null) metadata.setIsFavorite(false);
        
        var savedMetadata = metadataGateway.save(metadata);
        transaction.setMetadata(savedMetadata);
        
        final var savedTransaction = transactionGateway.save(transaction);
        log.info("Transaction created with id: {}", savedTransaction.getId());

        eventPublisher.publishEvent(new TransactionCreatedEvent(this, savedTransaction.getAccount().getId(), savedTransaction));
        return savedTransaction;
    }
}
