package com.freebills.gateways;

import com.freebills.domains.Transaction;
import com.freebills.domains.User;
import com.freebills.repositories.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public record TransactionGateway(TransactionRepository transactionRepository) {

    public Transaction save(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public Page<Transaction> findByUser(User user, final Pageable pageable) {
        return transactionRepository.findByAccount_User(user, pageable);
    }

    public Page<Transaction> findByUserDateFilter(final Long userId, final Integer month, final Integer year, final Pageable pageable, final String keyword) {
        LocalDate.now().getMonth().getValue();

        return transactionRepository.findByTransactionFilterByDate(userId, month, year, keyword, pageable);
    }

    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found!"));
    }

    public Transaction update(final Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void delete(final Long id) {
        transactionRepository.deleteById(id);
    }
}
