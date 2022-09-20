package com.freebills.gateways;

import com.freebills.domains.Transaction;
import com.freebills.exceptions.TransactionNotFoundException;
import com.freebills.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public record TransactionGateway(TransactionRepository transactionRepository) {

    public Transaction save(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Page<Transaction> findByUserDateFilter(final String login, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        return transactionRepository.findByTransactionFilterByDate(login, month, year, keyword, pageable);
    }

    public Page<Transaction> findByUserDateFilterRevenue(final String login, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        return transactionRepository.findByTransactionFilterByDateRevenue(login, month, year, keyword, pageable);
    }

    public Page<Transaction> findByUserDateFilterExpense(final String login, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        return transactionRepository.findByTransactionFilterByDateExpense(login, month, year, keyword, pageable);
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Not Found!"));
    }

    public Transaction update(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void delete(final Long id) {
        transactionRepository.deleteById(id);
    }
}
