package com.example.budget.features.budget;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.budget.features.budget.payload.request.CUBudgetRequest;
import com.example.budget.features.budget.payload.request.GetBudgetsByYearMonthRequest;
import com.example.budget.features.budget.payload.response.BudgetResponse;
import com.example.budget.features.category.Category;
import com.example.budget.features.category.CategoryRepository;
import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/budget")
@Log4j2
public class BudgetController {

        @Autowired
        private BudgetRepository budgetRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private UserAccountRepository userAccountRepository;

        public BudgetController() {

        }

        @GetMapping("")
        @PreAuthorize("hasRole('ROLE_USER')")
        public ResponseEntity<?> getBudgetsByYearMonth(@Valid @RequestBody GetBudgetsByYearMonthRequest requestBody) {
                // Get UserDetails from Security Context
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal();

                // Retrieve UserAccount
                UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

                // Get all categories
                List<Category> userCategories = authenticatedUser.getCategories();

                // Get all the budgets from user
                if (requestBody.getYearMonth() == null) {
                        List<Budget> budgetsFromRequest = budgetRepository
                                        .findByBudgetKeysCategoryIn(userCategories,
                                                        Sort.by("budgetKeys.yearMonth").descending()
                                                                        .and(Sort.by("amount").descending()));

                        return ResponseEntity.ok()
                                        .body(new BudgetResponse(HttpServletResponse.SC_OK,
                                                        "All budgets for user " + authenticatedUser.getUserName()
                                                                        + " retrieved successfully",
                                                        budgetsFromRequest));
                }

                List<Budget> budgetsFromRequest = budgetRepository
                                .findByBudgetKeysYearMonthAndBudgetKeysCategoryIn(requestBody.getYearMonth(),
                                                userCategories, Sort.by("amount").descending());

                return ResponseEntity.ok()
                                .body(new BudgetResponse(HttpServletResponse.SC_OK,
                                                "Budgets for " + requestBody.getYearMonth() + " retrieved successfully",
                                                budgetsFromRequest));
        }

        @PostMapping("")
        @PreAuthorize("hasRole('ROLE_USER')")
        public ResponseEntity<?> createBudgetByMonthAndCategory(@Valid @RequestBody CUBudgetRequest requestBody) {
                // Get UserDetails from Security Context
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal();

                log.info("Category Id:{}", requestBody.getCategoryId());

                // Retrieve UserAccount
                UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

                // Get category to attach the budget
                Category categoryFromRequest = categoryRepository.findByCategoryId(requestBody.getCategoryId());

                if (Objects.isNull(categoryFromRequest)) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BudgetResponse(
                                        HttpServletResponse.SC_NOT_FOUND, "Cannot find the category"));
                }

                // Check if the user own the category
                if (!categoryFromRequest.getUserAccount().equals(authenticatedUser)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BudgetResponse(
                                        HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
                }

                // Check if the budget already exist
                Budget foundBudget = budgetRepository.findByBudgetKeysYearMonthAndBudgetKeysCategory(
                                requestBody.getYearMonth(),
                                categoryFromRequest);

                if (foundBudget != null) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(new BudgetResponse(
                                        HttpServletResponse.SC_CONFLICT,
                                        "Budget already existed. If you want to update the budget, please use the update endpoint instead"));
                }

                // Create new budget based on request
                Budget newBudget = new Budget(new BudgetKeys(categoryFromRequest, requestBody.getYearMonth()),
                                requestBody.getAmount());

                Budget savedBudget = budgetRepository.save(newBudget);
                if (Objects.isNull(savedBudget)) {
                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BudgetResponse(
                                        HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                                        "Cannot create new budget, please try again"));
                }

                // Budget response
                List<Budget> budgetList = new ArrayList<>();
                budgetList.add(savedBudget);

                return ResponseEntity.ok()
                                .body(new BudgetResponse(HttpServletResponse.SC_OK, "Budget created successfully",
                                                budgetList));
        }

        @PutMapping("/{budgetId}")
        @PreAuthorize("hasRole('ROLE_USER')")
        public ResponseEntity<?> updateBudget(@PathVariable Long budgetId,
                        @Valid @RequestBody CUBudgetRequest requestBody) {

                // Get UserDetails from Security Context
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal();

                log.info("Category Id:{}", requestBody.getCategoryId());

                // Retrieve UserAccount
                UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

                // Get the budget from the repository
                Budget budgetFromRequest = budgetRepository.findByBudgetId(budgetId);

                if (budgetFromRequest == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BudgetResponse(
                                        HttpServletResponse.SC_NOT_FOUND, "Cannot find the budget"));
                }

                // Check if the user own the category
                if (!budgetFromRequest.getBudgetKeys().getCategory().getUserAccount().equals(authenticatedUser)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BudgetResponse(
                                        HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
                }

                if (requestBody.getAmount() != null) {
                        budgetFromRequest.setAmount(requestBody.getAmount());
                }

                Budget savedBudget = budgetRepository.save(budgetFromRequest);

                // Budget response
                List<Budget> budgetList = new ArrayList<>();
                budgetList.add(savedBudget);

                return ResponseEntity.ok()
                                .body(new BudgetResponse(HttpServletResponse.SC_OK, "Budget updated successfully",
                                                budgetList));
        }

        @DeleteMapping("/{budgetId}")
        @PreAuthorize("hasRole('ROLE_USER')")
        public ResponseEntity<?> deleteBudgetById(@PathVariable Long budgetId) {

                // Get UserDetails from Security Context
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal();

                // Retrieve UserAccount
                UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

                // Get the budget from the repository
                Budget budgetFromRequest = budgetRepository.findByBudgetId(budgetId);

                if (budgetFromRequest == null) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BudgetResponse(
                                        HttpServletResponse.SC_NOT_FOUND, "Cannot find the budget"));
                }

                // Check if the user own the category
                if (!budgetFromRequest.getBudgetKeys().getCategory().getUserAccount().equals(authenticatedUser)) {
                        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BudgetResponse(
                                        HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
                }

                // Delete budget
                budgetRepository.deleteByBudgetId(budgetId);

                // Budget response
                List<Budget> budgetList = new ArrayList<>();
                budgetList.add(budgetFromRequest);

                return ResponseEntity.ok()
                                .body(new BudgetResponse(HttpServletResponse.SC_OK,
                                                "Budget " + budgetId + " deleted successfully",
                                                budgetList));
        }
}
