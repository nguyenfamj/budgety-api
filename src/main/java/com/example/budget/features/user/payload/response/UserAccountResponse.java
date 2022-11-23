package com.example.budget.features.user.payload.response;

import com.example.budget.features.user.UserAccount;

public class UserAccountResponse {
    private int httpCode;
    private String message;
    private UserAccount user;

    public UserAccountResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public UserAccountResponse(int httpCode, String message, UserAccount user) {
        this.httpCode = httpCode;
        this.message = message;
        this.user = user;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return this.message;
    }

    public UserAccount getUser() {
        return this.user;
    }
}
