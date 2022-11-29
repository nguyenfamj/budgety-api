package com.example.budget.features.transaction.payee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.budget.features.user.UserAccount;

public interface PayeeRepository extends JpaRepository<Payee, Long> {
    Payee findByPayeeId(Long payeeId);

    Boolean existsByName(String name);

    Boolean existsByUserAccount(UserAccount userAccount);

    List<Payee> findByUserAccount(UserAccount userAccount);
}
