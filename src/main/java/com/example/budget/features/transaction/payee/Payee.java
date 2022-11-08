package com.example.budget.features.transaction.payee;

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

import com.example.budget.features.transaction.Transaction;
import com.example.budget.features.user.UserAccount;

@Entity
public class Payee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payeeId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "category", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public Payee() {
    }

    public Payee(String name, UserAccount userAccount) {
        this.name = name;
        this.userAccount = userAccount;
    }

    public Long getPayeeId() {
        return this.payeeId;
    }

    public String getName() {
        return this.name;
    }

    public UserAccount getUserAccount() {
        return this.userAccount;

    }

    public List<Transaction> getTransactions() {
        return this.transactions;
    }

    public void setName(String name) {
        this.name = name;
    }
}
