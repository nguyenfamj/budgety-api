package com.example.budget.features.transaction;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.budget.features.transaction.payee.Payee;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
    List<Transaction> findAllByPayee(Payee payee, Pageable pageable);
}
