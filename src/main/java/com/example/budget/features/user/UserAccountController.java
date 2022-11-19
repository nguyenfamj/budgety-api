package com.example.budget.features.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserAccountController {
    @Autowired
    UserAccountRepository userAccountRepository;

    @GetMapping("/all")
    @ResponseBody
    public List<UserAccount> users() {
        return (List<UserAccount>) userAccountRepository.findAll();
    }
}
