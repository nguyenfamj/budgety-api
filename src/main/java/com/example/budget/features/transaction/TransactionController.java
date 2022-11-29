package com.example.budget.features.transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.budget.features.account.Account;
import com.example.budget.features.account.AccountRepository;
import com.example.budget.features.category.Category;
import com.example.budget.features.category.CategoryRepository;
import com.example.budget.features.transaction.payee.Payee;
import com.example.budget.features.transaction.payee.PayeeRepository;
import com.example.budget.features.transaction.payload.request.CUTransactionRequest;
import com.example.budget.features.transaction.payload.response.TransactionResponse;
import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/transaction")
@Log4j2
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PayeeRepository payeeRepository;

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getTransactionsFromAllAccounts(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        try {

            List<Account> allAccounts = authenticatedUser.getAccounts();
            List<Long> accountIdList = new ArrayList<Long>();

            for (Account i : allAccounts) {
                accountIdList.add(i.getAccountId());
            }

            // Pagination
            Pageable sortedByTransactionDate = PageRequest.of(page, size, Sort.by("transactionDate").descending());

            // Get all the transactions from user with pagination
            List<Transaction> allTransactions = transactionRepository.findByAccountIn(allAccounts,
                    sortedByTransactionDate);

            return ResponseEntity.ok().body(new TransactionResponse(HttpServletResponse.SC_OK,
                    "Transactions retrieved successfully", allTransactions, page, size));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

    @GetMapping("/{transactionId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getSingleTransactionById(@PathVariable Long transactionId) {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the transaction from repository
        Transaction transactionFromRequest = transactionRepository.findByTransactionId(transactionId);

        // Check if the transaction exists
        if (transactionFromRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new TransactionResponse(HttpServletResponse.SC_NOT_FOUND, "Cannot find the transaction"));
        }

        // Check if the user own the transaction
        if (transactionFromRequest.getAccount().getUserAccount() != authenticatedUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TransactionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpServletResponse.SC_OK);
        response.put("message", "transaction retrieved successfully");
        response.put("transaction", transactionFromRequest);
        response.put("category", transactionFromRequest.getCategory());
        response.put("payee", transactionFromRequest.getPayee());
        response.put("account", transactionFromRequest.getAccount());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createTransaction(@Valid @RequestBody CUTransactionRequest requestBody) {

        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Category Id:{}", requestBody.getCategoryId());

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get category, payee and account on from the request
        Category categoryFromRequest = categoryRepository.findByCategoryId(requestBody.getCategoryId());
        Payee payeeFromRequest = payeeRepository.findByPayeeId(requestBody.getPayeeId());
        Account accountFromRequest = accountRepository.findByAccountId(requestBody.getAccountId());

        // Check if anything is null
        if (categoryFromRequest == null || payeeFromRequest == null || accountFromRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new TransactionResponse(HttpServletResponse.SC_NOT_FOUND, "Cannot find the request resources"));
        }

        // Check if the user own the resources
        if (categoryFromRequest.getUserAccount() != authenticatedUser
                || payeeFromRequest.getUserAccount() != authenticatedUser
                || accountFromRequest.getUserAccount() != authenticatedUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new TransactionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
        }

        // Create new transaction in the database
        Transaction newTransaction = new Transaction(requestBody.getTransactionDate(), requestBody.getAmount(),
                accountFromRequest, requestBody.getNote(), categoryFromRequest, payeeFromRequest);

        Transaction savedTransaction = transactionRepository.save(newTransaction);

        if (savedTransaction == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new TransactionResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Cannot create new transaction at the moment, please try again late"));
        }

        // Deduct the amount from the transaction in the account
        accountFromRequest
                .setAccountBalance(accountFromRequest.getAccountBalance().subtract(savedTransaction.getAmount()));

        // Save the account
        accountRepository.save(accountFromRequest);

        // Transaction Response
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(savedTransaction);

        return ResponseEntity.ok().body(new TransactionResponse(HttpServletResponse.SC_OK,
                "Transaction created successfully", transactionList, 0, 1));
    }

    @PutMapping("/{transactionId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updateTransaction(@PathVariable Long transactionId,
            @Valid @RequestBody CUTransactionRequest requestBody) {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the transaction from repository
        Transaction transactionFromRequest = transactionRepository.findByTransactionId(transactionId);

        // Check if the user own the transaction
        if (transactionFromRequest.getAccount().getUserAccount() != authenticatedUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TransactionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
        }

        // Get category, payee and account on from the request
        Category categoryFromRequest = categoryRepository.findByCategoryId(requestBody.getCategoryId());
        Payee payeeFromRequest = payeeRepository.findByPayeeId(requestBody.getPayeeId());
        Account accountFromRequest = accountRepository.findByAccountId(requestBody.getAccountId());

        // Set and update the field on the transaction
        if (requestBody.getTransactionDate() != null) {
            transactionFromRequest.setTransactionDate(requestBody.getTransactionDate());
        }

        // if () {
        // transactionFromRequest.setAccount(accountFromRequest);
        // }

        if (requestBody.getAmount() != null
                || (accountFromRequest != null && accountFromRequest.getUserAccount() == authenticatedUser)) {
            // Get the previous account balance
            transactionFromRequest.getAccount().setAccountBalance(
                    transactionFromRequest.getAccount().getAccountBalance().add(transactionFromRequest.getAmount()));

            // Change account if needed
            if (accountFromRequest != null && accountFromRequest.getUserAccount() == authenticatedUser) {
                transactionFromRequest.setAccount(accountFromRequest);
            }
            // Subtract the amount in the account balance
            if (requestBody.getAmount() != null) {
                transactionFromRequest.getAccount().setAccountBalance(
                        transactionFromRequest.getAccount().getAccountBalance().subtract(requestBody.getAmount()));
            }
        }

        if (requestBody.getNote() != null) {
            transactionFromRequest.setNote(requestBody.getNote());
        }

        if (categoryFromRequest != null && categoryFromRequest.getUserAccount() == authenticatedUser) {
            transactionFromRequest.setCategory(categoryFromRequest);
        }

        if (payeeFromRequest != null && payeeFromRequest.getUserAccount() == authenticatedUser) {
            transactionFromRequest.setPayee(payeeFromRequest);
        }

        Transaction savedTransaction = transactionRepository.save(transactionFromRequest);

        // Transaction response
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(savedTransaction);

        return ResponseEntity.ok().body(new TransactionResponse(HttpServletResponse.SC_OK,
                "Transaction updated successfully", transactionList, 0, 1));

    }

    @Transactional
    @DeleteMapping("/{transactionId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteTransactionById(@PathVariable Long transactionId) {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the transaction from repository
        Transaction transactionFromRequest = transactionRepository.findByTransactionId(transactionId);

        if (transactionFromRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new TransactionResponse(
                    HttpServletResponse.SC_NOT_FOUND, "Cannot find the transaction"));
        }

        // Check if the user own the transaction
        if (transactionFromRequest.getAccount().getUserAccount() != authenticatedUser) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new TransactionResponse(HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
        }

        // Revert the amount in account balance
        transactionFromRequest.getAccount().setAccountBalance(
                transactionFromRequest.getAccount().getAccountBalance().add(transactionFromRequest.getAmount()));

        // Delete the transaction
        transactionRepository.deleteByTransactionId(transactionId);

        // Transaction response
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transactionFromRequest);

        return ResponseEntity.ok().body(new TransactionResponse(HttpServletResponse.SC_OK,
                "Transaction deleted successfully", transactionList, 0, 1));
    }
}
