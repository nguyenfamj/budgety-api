package com.example.budget.features.auth;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.budget.features.auth.payload.request.LoginRequest;
import com.example.budget.features.auth.payload.request.RegisterRequest;
import com.example.budget.features.auth.payload.response.LoginResponse;
import com.example.budget.features.auth.payload.response.ServerResponse;
import com.example.budget.features.role.Role;
import com.example.budget.features.role.RoleEnum;
import com.example.budget.features.role.RoleRepository;
import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;
import com.example.budget.security.jwt.JwtUtils;
import com.example.budget.services.UserDetails.UserDetailsImpl;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/auth")
@Log4j2
public class AuthController {

        @Autowired
        AuthenticationManager authenticationManager;

        @Autowired
        private UserAccountRepository userAccountRepository;

        @Autowired
        private RoleRepository roleRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        JwtUtils jwtUtils;

        public AuthController() {
        }

        @GetMapping("")
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        public String index() {
                return "Welcome to the personal budget application!";
        }

        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
                // Authenticate user
                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),
                                                loginRequest.getPassword()));

                log.info("Username: {}", loginRequest.getUserName());

                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("New LOG: {}", authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

                log.info("Username: {}", userDetails.getUsername());

                ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

                List<String> roles = userDetails.getAuthorities().stream()
                                .map(item -> item.getAuthority())
                                .collect(Collectors.toList());

                log.debug("Log:{}", jwtCookie);

                LoginResponse loginResponse = new LoginResponse(userDetails.getUserId(), userDetails.getUsername(),
                                userDetails.getFirstName(), userDetails.getLastName(), roles);

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(loginResponse);
        }

        @PostMapping("/register")
        public ResponseEntity<?> registerNormalUser(@Valid @RequestBody RegisterRequest registerRequest) {
                if (userAccountRepository.existsByUserName(registerRequest.getUserName())) {
                        return ResponseEntity.badRequest().body(new ServerResponse(HttpServletResponse.SC_NOT_FOUND,
                                        "Error: Username cannot be used, please pick another one and try again!"));
                }

                // Create new user based on the request
                UserAccount user = new UserAccount(registerRequest.getUserName(), registerRequest.getFirstName(),
                                registerRequest.getLastName(), passwordEncoder.encode(registerRequest.getPassword()));

                log.info(registerRequest);

                Set<Role> roles = new HashSet<>();

                // Set role User as a default for new user in this register api path
                // /api/v1/auth/register
                Role userRole = roleRepository.findByType(RoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found in the database"));

                roles.add(userRole);
                user.setRoles(roles);

                // Save user in the database
                userAccountRepository.save(user);

                // Response
                return ResponseEntity
                                .ok(new ServerResponse(HttpServletResponse.SC_OK, "User registered", user.getUserName(),
                                                user.getFirstName(), user.getLastName()));
        }
}
