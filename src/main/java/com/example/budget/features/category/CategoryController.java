package com.example.budget.features.category;

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

import com.example.budget.features.category.payload.request.CategoryInfoRequest;
import com.example.budget.features.category.payload.response.CategoryResponse;
import com.example.budget.features.user.UserAccount;
import com.example.budget.features.user.UserAccountRepository;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/category")
@Log4j2
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    public CategoryController() {

    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllCategories() {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        if (!categoryRepository.existsByUserAccount(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CategoryResponse(HttpServletResponse.SC_NOT_FOUND, "No category found on this account"));
        }

        // Get categories from the database
        List<Category> categories = categoryRepository.findByUserAccount(authenticatedUser);

        return ResponseEntity.ok()
                .body(new CategoryResponse(HttpServletResponse.SC_OK, "Categories retrieved", categories));

    }

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryInfoRequest categoryInfoRequest) {
        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Category Name:{}", categoryInfoRequest.getCategoryName());

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Create new category
        Category newCategory = new Category(categoryInfoRequest.getCategoryName(), authenticatedUser);

        Category savedCategory = categoryRepository.save(newCategory);
        if (Objects.isNull(savedCategory)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CategoryResponse(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cannot create new category, please try again"));
        }

        // Category Response
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(savedCategory);

        return ResponseEntity.ok()
                .body(new CategoryResponse(HttpServletResponse.SC_OK, "Category created successfully", categoryList));

    }

    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updateCategory(@PathVariable Long categoryId,
            @Valid @RequestBody CategoryInfoRequest categoryInfoRequest) {

        log.info("New update for category: {}", categoryId);

        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        log.info("Category Name:{}", categoryInfoRequest.getCategoryName());

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the category that needs to be modified
        Category categoryFromRequest = categoryRepository.findByCategoryId(categoryId);

        if (Objects.isNull(categoryFromRequest)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CategoryResponse(
                    HttpServletResponse.SC_NOT_FOUND, "Cannot find the category"));
        }

        // Check if the user own the category
        if (!categoryFromRequest.getUserAccount().equals(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CategoryResponse(
                    HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
        }

        // Modify the content of the category
        categoryFromRequest.setName(categoryInfoRequest.getCategoryName());

        // Save the category
        Category savedCategory = categoryRepository.save(categoryFromRequest);

        // Category Response
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(savedCategory);

        return ResponseEntity.ok()
                .body(new CategoryResponse(HttpServletResponse.SC_OK, "Category saved successfully", categoryList));
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        log.info("Delete category: {}", categoryId);

        // Get UserDetails from Security Context
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Retrieve UserAccount
        UserAccount authenticatedUser = userAccountRepository.findByUserName(userDetails.getUsername());

        // Get the category that needs to be modified
        Category categoryFromRequest = categoryRepository.findByCategoryId(categoryId);

        if (Objects.isNull(categoryFromRequest)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CategoryResponse(
                    HttpServletResponse.SC_NOT_FOUND, "Cannot find the category"));
        }

        // Check if the user own the category
        if (!categoryFromRequest.getUserAccount().equals(authenticatedUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CategoryResponse(
                    HttpServletResponse.SC_UNAUTHORIZED, "Access forbidden"));
        }

        categoryRepository.deleteById(categoryId);

        // Category Response
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(categoryFromRequest);

        return ResponseEntity.ok().body(new CategoryResponse(HttpServletResponse.SC_OK,
                "Category " + categoryId + " deleted", categoryList));
    }
}
