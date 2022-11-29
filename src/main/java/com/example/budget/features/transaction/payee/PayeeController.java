package com.example.budget.features.transaction.payee;

import java.util.ArrayList;
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

import com.example.budget.features.transaction.payee.payload.request.CUPayeeRequest;
import com.example.budget.features.transaction.payee.payload.response.PayeeResponse;
import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/payee")
@Log4j2
public class PayeeController {

    @Autowired
    private PayeeRepository payeeRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    public PayeeController() {

    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllPayees() {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        if (!payeeRepository.existsByUserAccount(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayeeResponse(HttpServletResponse.SC_NOT_FOUND, "No category found on this account"));
        }

        List<Payee> payees = payeeRepository.findByUserAccount(authenticatedUser);

        return ResponseEntity.ok()
                .body(new PayeeResponse(HttpServletResponse.SC_OK, "Payee list retrieved successfully", payees));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createPayee(@Valid @RequestBody CUPayeeRequest requestBody) {

        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Payee Name:{}", requestBody.getPayeeName());

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Create new payee
        Payee newPayee = new Payee(requestBody.getPayeeName(), authenticatedUser);

        Payee savedPayee = payeeRepository.save(newPayee);
        if (Objects.isNull(savedPayee)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new PayeeResponse(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot create new payee, please try again"));
        }

        // Payee Response
        List<Payee> payeeList = new ArrayList<>();
        payeeList.add(savedPayee);

        return ResponseEntity.ok()
                .body(new PayeeResponse(HttpServletResponse.SC_OK, "Payee created successfully", payeeList));
    }

    @PutMapping("/{payeeId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updatePayee(@PathVariable Long payeeId, @Valid @RequestBody CUPayeeRequest requestBody) {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Category Name:{}", requestBody.getPayeeName());

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the category that needs to be modified
        Payee payeeFromRequest = payeeRepository.findByPayeeId(payeeId);

        if (Objects.isNull(payeeFromRequest)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new PayeeResponse(
                    HttpServletResponse.SC_NOT_FOUND, "Cannot find the payee"));
        }

        // Check if the user own the payee
        if (!payeeFromRequest.getUserAccount().equals(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PayeeResponse(
                    HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
        }

        // Modify the content of the category
        payeeFromRequest.setName(requestBody.getPayeeName());

        // Save the payee
        Payee savedPayee = payeeRepository.save(payeeFromRequest);

        // Category Response
        List<Payee> payeeList = new ArrayList<>();
        payeeList.add(savedPayee);

        return ResponseEntity.ok()
                .body(new PayeeResponse(HttpServletResponse.SC_OK, "Payee saved successfully", payeeList));
    }

    @DeleteMapping("/{payeeId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deletePayee(@PathVariable Long payeeId) {
        log.info("Delete payee: {}", payeeId);

        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the payee to be modified
        Payee payeeFromRequest = payeeRepository.findByPayeeId(payeeId);

        if (Objects.isNull(payeeFromRequest)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new PayeeResponse(HttpServletResponse.SC_NOT_FOUND, "Cannot find the payee"));
        }

        // Check if the user own the payee
        if (!payeeFromRequest.getUserAccount().equals(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new PayeeResponse(
                    HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
        }

        payeeRepository.deleteById(payeeId);

        // Payee Response
        List<Payee> payeeList = new ArrayList<>();
        payeeList.add(payeeFromRequest);

        return ResponseEntity.ok()
                .body(new PayeeResponse(HttpServletResponse.SC_OK, "Payee " + payeeId + " deleted", payeeList));
    }
}
