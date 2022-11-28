package com.example.budget.features.category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.budget.features.user.UserAccount;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUserAccount(UserAccount userAccount);

    Category findByCategoryId(Long categoryId);

    Boolean existsByUserAccount(UserAccount userAccount);
}
