package com.example.budget.features.account;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.budget.features.account.payload.request.AccountInfoRequest;
import com.example.budget.features.account.payload.response.AccountResponse;
import com.example.budget.features.account.payload.response.CUAccountResponse;
import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/accounts")
@Log4j2
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    public AccountController() {

    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllAccounts() {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        if (!accountRepository.existsByUserAccount(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new AccountResponse(HttpServletResponse.SC_NOT_FOUND, "No account found"));
        }

        // Get accounts from database
        List<Account> accounts = accountRepository.findByUserAccount(authenticatedUser);

        return ResponseEntity.ok().body(
                new AccountResponse(HttpServletResponse.SC_OK, "Accounts retrieved", authenticatedUser, accounts));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createNewAccount(@Valid @RequestBody AccountInfoRequest accountInfoRequest) {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Create new account
        Account newAccount = new Account(accountInfoRequest.getAccountName(),
                accountInfoRequest.getInitialBalance(), authenticatedUser);

        Account savedAccount = accountRepository.save(newAccount);
        if (Objects.isNull(savedAccount)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CUAccountResponse(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot create new account, please try again"));
        }
        return ResponseEntity.ok().body(
                new CUAccountResponse(HttpServletResponse.SC_OK, "Account created successfully", savedAccount));
    }

    @PutMapping("/{accountId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updateAccount(@PathVariable Long accountId,
            @Valid @RequestBody AccountInfoRequest accountInfoRequest) {

        log.info("Acount input info: {} {}", accountInfoRequest.getAccountName(),
                accountInfoRequest.getInitialBalance());
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the account that needs to be modified
        Account accountFromRequest = accountRepository.findByAccountId(accountId);

        if (Objects.isNull(accountFromRequest)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CUAccountResponse(
                    HttpServletResponse.SC_NOT_FOUND, "Cannot find the account"));
        }

        // Check if the user own the account
        if (!accountFromRequest.getUserAccount().equals(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CUAccountResponse(
                    HttpServletResponse.SC_UNAUTHORIZED, "You do not have access to this account"));
        }

        // Modify the account info
        if (!Objects.isNull(accountFromRequest.getAccountName())) {
            accountFromRequest.setAccountName(accountInfoRequest.getAccountName());
            log.info("accountName changed: {}", accountFromRequest.getAccountName());
        }
        if (!Objects.isNull(accountFromRequest.getAccountBalance())) {
            accountFromRequest.setAccountBalance(accountInfoRequest.getInitialBalance());
            log.info("accountBalance changed: {}", accountFromRequest.getAccountBalance());
        }

        // Save the account
        Account savedAccount = accountRepository.save(accountFromRequest);

        log.info("New info: {} {}", savedAccount.getAccountName(), savedAccount.getAccountBalance());

        return ResponseEntity.ok()
                .body(new CUAccountResponse(HttpServletResponse.SC_OK, "Account saved successfully", savedAccount));
    }

    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {

        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the account that needs to be modified
        Account accountFromRequest = accountRepository.findByAccountId(accountId);

        if (Objects.isNull(accountFromRequest)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CUAccountResponse(
                    HttpServletResponse.SC_NOT_FOUND, "Cannot find the account"));
        }

        // Check if the user own the account
        if (!accountFromRequest.getUserAccount().equals(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CUAccountResponse(
                    HttpServletResponse.SC_UNAUTHORIZED, "You do not have access to this account"));
        }

        // Delete the account
        accountRepository.deleteById(accountId);

        return ResponseEntity.ok().body(new CUAccountResponse(HttpServletResponse.SC_OK,
                "Account " + accountId + " deleted", accountFromRequest));
    }

}
