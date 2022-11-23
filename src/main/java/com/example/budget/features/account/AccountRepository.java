package com.example.budget.features.account;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.budget.features.user.UserAccount;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findByUserAccount(UserAccount userAccount);

    Account findByAccountId(Long accountId);

    Boolean existsByUserAccount(UserAccount userAccount);
}
