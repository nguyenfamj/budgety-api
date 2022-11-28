package com.example.budget.features.budget.payload.request;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CUBudgetRequest {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date yearMonth;

    private Long categoryId;

    private BigDecimal amount;

    public CUBudgetRequest(Date yearMonth, Long categoryId, BigDecimal amount) {
        this.yearMonth = yearMonth;
        this.categoryId = categoryId;
        this.amount = amount;
    }

    public Date getYearMonth() {
        return this.yearMonth;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

}
