package com.example.budget.features.account;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;

import com.example.budget.features.transaction.Transaction;
import com.example.budget.features.user.UserAccount;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private BigDecimal accountBalance;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "transactionId", fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    public Account() {

    }

    public Account(String accountName, BigDecimal initialBalance, UserAccount userAccount) {
        this.accountName = accountName;
        this.accountBalance = initialBalance;
        this.userAccount = userAccount;
    }

    @PreRemove
    public void preRemove() {
        this.userAccount.getAccounts().clear();
        this.userAccount = null;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public BigDecimal getAccountBalance() {
        return this.accountBalance;
    }

    public UserAccount getUserAccount() {
        return this.userAccount;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

}
