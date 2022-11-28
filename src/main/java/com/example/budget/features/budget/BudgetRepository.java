package com.example.budget.features.budget;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.budget.features.category.Category;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByBudgetKeysYearMonthAndBudgetKeysCategoryIn(Date yearMonth,
            List<Category> categories, Sort sort);

    List<Budget> findByBudgetKeysCategoryIn(List<Category> categories, Sort sort);

    Budget findByBudgetKeysYearMonthAndBudgetKeysCategory(Date yearMonth, Category category);

    Budget findByBudgetId(Long budgetId);

    @Transactional
    List<Budget> deleteByBudgetId(Long budgetId);
}
