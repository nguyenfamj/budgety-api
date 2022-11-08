package com.example.budget.features.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.YearMonth;

import javax.persistence.Convert;

import com.example.budget.features.transaction.category.Category;
import com.example.budget.services.EntityConverter.YearMonthAttributeConverter;

@Entity
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(columnDefinition = "date", unique = true, nullable = false)
    @Convert(converter = YearMonthAttributeConverter.class)
    private YearMonth yearMonth;

    public Budget() {

    }

    public Budget(Category category, BigDecimal amount, YearMonth yearMonth) {
        this.category = category;
        this.amount = amount;
        this.yearMonth = yearMonth;
    }

    public Long getBudgetId() {
        return this.budgetId;
    }

    public Category getCategory() {
        return this.category;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public YearMonth getYearMonth() {
        return this.yearMonth;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setYearMonth(YearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }
}
