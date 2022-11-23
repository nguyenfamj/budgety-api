package com.example.budget.features.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findByUserName(String userName);

    UserAccount findByUserId(Long userId);

    Boolean existsByUserName(String userName);

    Boolean existsByUserId(Long userId);

}
