package com.example.budget.features.budget.payload.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetBudgetsByYearMonthRequest {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date yearMonth;

    @JsonCreator
    public GetBudgetsByYearMonthRequest(@JsonProperty("yearMonth") Date yearMonth) {
        super();
        this.yearMonth = yearMonth;
    }

    public Date getYearMonth() {
        return this.yearMonth;
    }
}
