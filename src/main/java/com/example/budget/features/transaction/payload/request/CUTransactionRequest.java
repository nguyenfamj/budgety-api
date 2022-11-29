package com.example.budget.features.transaction.payload.request;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CUTransactionRequest {
    private Long accountId;
    private Long categoryId;
    private Long payeeId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date transactionDate;

    private BigDecimal amount;
    private String note;

    public CUTransactionRequest(Long accountId, Long categoryId, Long payeeId, Date transactionDate,
            BigDecimal amount, String note) {
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.payeeId = payeeId;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.note = note;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public Long getCategoryId() {
        return this.categoryId;
    }

    public Long getPayeeId() {
        return this.payeeId;
    }

    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public String getNote() {
        return this.note;
    }
}
