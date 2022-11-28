package com.example.budget.features.budget.payload.response;

import java.util.List;

import com.example.budget.features.budget.Budget;

public class BudgetResponse {
    private int httpCode;
    private String message;
    private List<Budget> budget;

    public BudgetResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public BudgetResponse(int httpCode, String message, List<Budget> budget) {
        this.httpCode = httpCode;
        this.message = message;

        this.budget = budget;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return this.message;
    }

    public List<Budget> getBudget() {
        return this.budget;
    }
}
