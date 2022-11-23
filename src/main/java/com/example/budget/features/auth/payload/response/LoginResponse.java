package com.example.budget.features.auth.payload.response;

import java.util.List;

public class LoginResponse {
    Long userId;
    String userName;
    String firstName;
    String lastName;
    List<String> roles;

    public LoginResponse() {
    }

    public LoginResponse(Long userId, String userName, String firstName, String lastName, List<String> roles) {
        this.userId = userId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public Long getUserId() {
        return this.userId;
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

    public List<String> getRoles() {
        return this.roles;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
