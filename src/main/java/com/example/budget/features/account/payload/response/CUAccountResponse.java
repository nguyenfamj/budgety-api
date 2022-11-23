package com.example.budget.features.account.payload.response;

import com.example.budget.features.account.Account;

public class CUAccountResponse {
    private int httpCode;
    private String message;
    private Account accounts;

    public CUAccountResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public CUAccountResponse(int httpCode, String message, Account accounts) {
        this.httpCode = httpCode;
        this.message = message;

        this.accounts = accounts;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return this.message;
    }

    public Account getAccounts() {
        return this.accounts;
    }

}
