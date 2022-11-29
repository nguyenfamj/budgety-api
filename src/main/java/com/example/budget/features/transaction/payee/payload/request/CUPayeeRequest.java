package com.example.budget.features.transaction.payee.payload.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CUPayeeRequest {
    private String payeeName;

    @JsonCreator
    public CUPayeeRequest(@JsonProperty("payeeName") String payeeName) {
        super();
        this.payeeName = payeeName;
    }

    public String getPayeeName() {
        return this.payeeName;
    }
}
