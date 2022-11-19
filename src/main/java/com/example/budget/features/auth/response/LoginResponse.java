package com.example.budget.features.auth.response;

import java.util.List;

import lombok.Data;

@Data
public class LoginResponse {
    Long userId;
    String userName;
    String firstName;
    String lastName;
    List<String> roles;
}
