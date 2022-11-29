package com.example.budget.features.transaction;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.budget.features.account.Account;
import com.example.budget.features.transaction.payee.Payee;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByPayee(Payee payee, Pageable pageable);

    Transaction findByTransactionId(Long transactionId);

    List<Transaction> findByAccountIn(List<Account> accounts, Pageable pageable);

    void deleteByTransactionId(Long transactionId);

}
