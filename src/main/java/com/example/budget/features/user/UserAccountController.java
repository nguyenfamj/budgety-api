package com.example.budget.features.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserAccountController {

    @GetMapping("/new")
    public String user() {
        return "Hi there user";
    }
}
