package com.example.wgu.finance_tracker_backend.DTOs;

import java.math.BigDecimal;

public class AccountResponse {
    private Long id;
    private String accountName;
    private BigDecimal balance;
    private String accountType; // Using String to represent the AccountType enum for the client

    // Constructors, Getters, and Setters
    public AccountResponse() {
    }

    public AccountResponse(Long id, String accountName, BigDecimal balance, String accountType) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

}
