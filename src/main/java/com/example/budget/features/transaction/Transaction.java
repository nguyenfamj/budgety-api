package com.example.budget.features.transaction;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.budget.features.account.Account;
import com.example.budget.features.category.Category;
import com.example.budget.features.transaction.payee.Payee;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date transactionDate;

    @Column(nullable = false)
    private BigDecimal amount;

    private String note;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "payeeId")
    private Payee payee;

    public Transaction() {

    }

    public Transaction(Date transactionDate, BigDecimal amount, Account account, Category category, Payee payee) {
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.account = account;
        this.category = category;
        this.payee = payee;
    }

    public Transaction(Date transactionDate, BigDecimal amount, Account account, String note, Category category,
            Payee payee) {
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.account = account;
        this.category = category;
        this.note = note;
        this.payee = payee;
    }

    @PreRemove
    public void preRemove() {
        this.account.getTransactions().clear();
        this.category.getTransactions().clear();
        this.payee.getTransactions().clear();

        //
        this.account = null;
        this.category = null;
        this.payee = null;
    }

    public Long getTransactionId() {
        return this.transactionId;
    }

    public Date getTransactionDate() {
        return this.transactionDate;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Account getAccount() {
        return this.account;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getNote() {
        return this.note;
    }

    public Payee getPayee() {
        return this.payee;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
