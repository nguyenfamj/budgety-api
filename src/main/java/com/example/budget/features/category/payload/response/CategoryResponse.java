package com.example.budget.features.category.payload.response;

import java.util.List;

import com.example.budget.features.category.Category;

public class CategoryResponse {
    private int httpCode;
    private String message;
    private List<Category> category;

    public CategoryResponse(int httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }

    public CategoryResponse(int httpCode, String message, List<Category> category) {
        this.httpCode = httpCode;
        this.message = message;

        this.category = category;
    }

    public int getHttpCode() {
        return this.httpCode;
    }

    public String getMessage() {
        return this.message;
    }

    public List<Category> getCategory() {
        return this.category;
    }
}
