package com.example.budget.features.user;

import org.springframework.http.HttpHeaders;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.budget.features.user.payload.request.UserUpdateRequest;
import com.example.budget.features.user.payload.response.UserAccountResponse;
import com.example.budget.security.jwt.JwtUtils;
import com.example.budget.services.UserDetails.UserDetailsImpl;
import com.example.budget.services.UserDetails.UserDetailsServiceImpl;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/users")
@Log4j2
public class UserAccountController {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseBody
    public List<UserAccount> users() {
        return (List<UserAccount>) userAccountRepository.findAll();
    }

    // @Access: Public
    // @Path: /api/v1/users/{id}
    @GetMapping("/self")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getUserByUserName() {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        return ResponseEntity.ok().body(authenticatedUser);

    }

    @PutMapping("")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateExistingUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("User update request: {}, {}, {}", userUpdateRequest.getFirstName(), userUpdateRequest.getUserName(),
                userUpdateRequest.getPassword());

        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (userAccountRepository.existsByUserName(userUpdateRequest.getUserName())
                && userDetails.getUsername() != userUpdateRequest.getUserName()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new UserAccountResponse(
                    HttpServletResponse.SC_UNAUTHORIZED, "Cannot use this username, please try another one"));
        }

        // Get existing user
        UserAccount existingUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Set updated fields
        if (Objects.isNull(userUpdateRequest.getPassword())) {
            existingUser.updateUserWithoutPassword(userUpdateRequest.getUserName(), userUpdateRequest.getFirstName(),
                    userUpdateRequest.getLastName());
        } else {
            existingUser.updateUserWithPassword(userUpdateRequest.getUserName(), userUpdateRequest.getFirstName(),
                    userUpdateRequest.getLastName(), passwordEncoder.encode(userUpdateRequest.getPassword()));
        }
        userAccountRepository.save(existingUser);

        // Refresh the token
        UserDetailsImpl newUserDetails = (UserDetailsImpl) userDetailsService
                .loadUserByUsername(existingUser.getUserName());

        log.info("new Username: {}", newUserDetails.getUsername());
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(newUserDetails);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserAccountResponse(HttpServletResponse.SC_OK, "User updated successfully", existingUser));
    }

    @DeleteMapping("/{userName}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String userName) {

        UserAccount foundUser = userAccountRepository.findByUserName(userName);

        if (Objects.isNull(foundUser)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new UserAccountResponse(HttpServletResponse.SC_NOT_FOUND, "Cannot found this user"));
        }

        // Delete the found user
        userAccountRepository.delete(foundUser);

        return ResponseEntity.ok()
                .body(new UserAccountResponse(HttpServletResponse.SC_OK, "User deleted", foundUser));
    }
}
