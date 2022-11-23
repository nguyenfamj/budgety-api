package com.example.budget.features.user.payload.request;

public class UserUpdateRequest {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;

    public UserUpdateRequest(String userName, String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.password = password;
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

    public String getPassword() {
        return this.password;
    }
}
