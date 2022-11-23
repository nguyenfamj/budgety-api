package com.example.budget.features.auth.payload.response;

public class ServerResponse {
    private int httpCode;
    private String message;
    private String userName;
    private String firstName;
    private String lastName;

    public ServerResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public ServerResponse(int httpCode, String message, String userName, String firstName, String lastName) {
        this.httpCode = httpCode;
        this.message = message;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return this.message;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }
}
