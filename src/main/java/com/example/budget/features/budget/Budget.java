package com.example.budget.features.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.example.budget.features.category.Category;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

import javax.persistence.EmbeddedId;

@Entity
public class Budget {

    @EmbeddedId
    private BudgetKeys budgetKeys;

    @Column(columnDefinition = "serial", unique = true, updatable = false, insertable = false)
    private Long budgetId;

    @Column(nullable = false)
    private BigDecimal amount;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categoryId", insertable = false, updatable = false)
    private Category category;

    public Budget() {

    }

    public Budget(BudgetKeys budgetKeys, BigDecimal amount) {
        this.budgetKeys = budgetKeys;
        this.amount = amount;
    }

    public BudgetKeys getBudgetKeys() {
        return this.budgetKeys;
    }

    public void setBudgetKeys(BudgetKeys budgetKeys) {
        this.budgetKeys = budgetKeys;
    }

    public Long getBudgetId() {
        return this.budgetId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
