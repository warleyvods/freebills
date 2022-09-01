package com.freebills.gateways;

import com.freebills.domains.Transaction;
import com.freebills.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record TransactionGateway(TransactionRepository transactionRepository) {

    public Transaction save(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction findById(Long id){
        return transactionRepository.findById(id).orElseThrow(()-> new RuntimeException("Not Found!"));
    }

    public Transaction update(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void delete(final Long id) {
        transactionRepository.deleteById(id);
    }
}
