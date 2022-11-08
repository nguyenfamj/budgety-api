package com.example.budget.features.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.budget.features.token.TokenService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("")
    public String index() {
        return "Welcome to the personal budget application!";
    }

    @PostMapping("/token")
    public String token(Authentication authentication) {
        LOG.debug("User '{}' requested for token", authentication.getName());
        String token = tokenService.createToken(authentication);
        LOG.debug("Token granted: {}", token);
        return token;
    }
}
