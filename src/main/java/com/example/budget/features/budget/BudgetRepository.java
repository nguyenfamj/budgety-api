package com.example.budget.features.budget;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface BudgetRepository extends PagingAndSortingRepository<Budget, Long> {

}
