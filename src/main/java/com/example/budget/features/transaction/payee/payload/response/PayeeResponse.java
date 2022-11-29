package com.example.budget.features.transaction.payee.payload.response;

import java.util.List;

import com.example.budget.features.transaction.payee.Payee;

public class PayeeResponse {
    private int httpCode;
    private String message;
    private List<Payee> payee;

    public PayeeResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public PayeeResponse(int httpCode, String message, List<Payee> payee) {
        this.httpCode = httpCode;
        this.message = message;
        this.payee = payee;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return this.message;
    }

    public List<Payee> getPayee() {
        return this.payee;
    }
}
