package com.example.budget.features.transaction.payload.response;

import java.util.List;

import com.example.budget.features.transaction.Transaction;

public class TransactionResponse {
    private int httpCode;
    private int page;
    private int size;
    private String message;
    private List<Transaction> transactions;

    public TransactionResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public TransactionResponse(int httpCode, String message, int page, int size) {
        this.httpCode = httpCode;
        this.message = message;
        this.page = page;
        this.size = size;
    }

    public TransactionResponse(int httpCode, String message, List<Transaction> transactions, int page, int size) {
        this.httpCode = httpCode;
        this.message = message;
        this.transactions = transactions;
        this.page = page;
        this.size = size;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return this.message;
    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public int getPage() {
        return this.page;
    }

    public int getSize() {
        return this.size;
    }
}
