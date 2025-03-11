package com.freebills.usecases;

import com.freebills.domain.Transaction;
import com.freebills.domain.TransactionMetadata;
import com.freebills.events.transaction.TransactionUpdatedEvent;
import com.freebills.gateways.TransactionGateway;
import com.freebills.gateways.TransactionMetadataGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateTransaction {

    private final TransactionGateway transactionGateway;
    private final TransactionMetadataGateway metadataGateway;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Transaction execute(final Transaction transaction) {
        // Buscar a transação original para ter o oldTransaction
        final var oldTransaction = transactionGateway.findById(transaction.getId());
        
        // Atualizar metadados baseados na transação
        TransactionMetadata metadata = transaction.getMetadata();
        if (metadata == null) {
            metadata = TransactionMetadata.builder().build();
        }
        
        // Atualizamos os campos derivados
        metadata.setHasReceipt(transaction.getReceipt() != null);
        metadata.setHasObservation(transaction.getObservation() != null && !transaction.getObservation().isEmpty());
        
        // Se não foram especificados, mantemos os valores existentes ou inicializamos com valores padrão
        if (metadata.getIsRecurring() == null) metadata.setIsRecurring(false);
        if (metadata.getIsFixed() == null) metadata.setIsFixed(false);
        if (metadata.getIsCreditCardPayment() == null) metadata.setIsCreditCardPayment(false);
        if (metadata.getIsBankSlip() == null) metadata.setIsBankSlip(false);
        if (metadata.getHasPaidConfirmation() == null) metadata.setHasPaidConfirmation(false);
        if (metadata.getTags() == null) metadata.setTags("");
        if (metadata.getIsFavorite() == null) metadata.setIsFavorite(false);
        
        if (metadata.getId() == null) {
            var savedMetadata = metadataGateway.save(metadata);
            transaction.setMetadata(savedMetadata);
        } else {
            metadataGateway.save(metadata);
        }
        
        final var savedTransaction = transactionGateway.update(transaction);
        log.info("Transaction updated with id: {}", savedTransaction.getId());
        
        // Corrigi a chamada do evento para incluir o accountId e oldTransaction
        eventPublisher.publishEvent(new TransactionUpdatedEvent(
            this, 
            savedTransaction.getAccount().getId(), 
            savedTransaction, 
            oldTransaction
        ));

        return savedTransaction;
    }
}
