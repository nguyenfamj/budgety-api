package com.example.budget.features.account.payload.response;

import java.util.List;

import com.example.budget.features.account.Account;
import com.example.budget.features.user.UserAccount;

public class AccountResponse {

    private int httpCode;
    private String message;
    private UserAccount user;
    private List<Account> accounts;

    public AccountResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public AccountResponse(int httpCode, String message, UserAccount user, List<Account> accounts) {
        this.httpCode = httpCode;
        this.message = message;
        this.user = user;
        this.accounts = accounts;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return this.message;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public UserAccount getUserAccount() {
        return this.user;
    }
}
