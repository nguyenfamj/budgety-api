package com.example.budget.features.category.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CategoryInfoRequest {

    private String categoryName;

    @JsonCreator
    public CategoryInfoRequest(@JsonProperty("categoryName") String categoryName) {
        super();
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return this.categoryName;
    }
}
