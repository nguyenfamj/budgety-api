package com.example.budget.features.user;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.example.budget.features.account.Account;
import com.example.budget.features.transaction.category.Category;
import com.example.budget.features.transaction.payee.Payee;

import javax.persistence.GenerationType;

@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String userName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userAccount", fetch = FetchType.EAGER)
    private List<Account> accounts;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userAccount", fetch = FetchType.LAZY)
    private List<Category> categories;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userAccount", fetch = FetchType.LAZY)
    private List<Payee> payees;

    public UserAccount() {
    }

    public UserAccount(String userName, String firstName, String lastName, String hashedPassword, String role) {
        super();
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hashedPassword = hashedPassword;
        this.role = role;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public String getRole() {
        return this.role;
    }

    public List<Account> getAccounts() {
        return this.accounts;
    }

    public List<Category> getCategories() {
        return this.categories;
    }

    public List<Payee> getPayees() {
        return this.payees;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
