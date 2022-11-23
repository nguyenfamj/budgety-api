package com.example.budget.features.account.payload.request;

import java.math.BigDecimal;

public class AccountInfoRequest {
    private String accountName;
    private BigDecimal initialBalance;

    public AccountInfoRequest(String accountName, BigDecimal initialBalance) {
        this.accountName = accountName;
        this.initialBalance = initialBalance;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public BigDecimal getInitialBalance() {
        return this.initialBalance;
    }
}
