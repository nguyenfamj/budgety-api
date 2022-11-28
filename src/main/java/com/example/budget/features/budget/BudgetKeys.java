package com.example.budget.features.budget;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.budget.features.category.Category;

@Embeddable
public class BudgetKeys implements Serializable {
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @Temporal(TemporalType.DATE)
    @Column(columnDefinition = "date", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date yearMonth;

    public BudgetKeys() {
    }

    public BudgetKeys(Category category, Date yearMonth) {
        this.category = category;
        this.yearMonth = yearMonth;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getYearMonth() {
        return this.yearMonth;
    }

    public void setYearMonth(Date yearMonth) {
        this.yearMonth = yearMonth;
    }
}
