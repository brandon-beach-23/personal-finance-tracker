package com.example.wgu.finance_tracker_backend.DTOs;

import java.math.BigDecimal;

public class AccountResponse {
    private Long id;
    private Long userId;
    private String accountName;
    private BigDecimal balance;
    private String accountType; // Using String to represent the AccountType enum for the client
    private BigDecimal interestRate;

    // Constructors, Getters, and Setters
    public AccountResponse() {
    }

    public AccountResponse(Long id, Long userId, String accountName, BigDecimal balance, String accountType) {
        this.id = id;
        this.userId = userId;
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
    }

    public AccountResponse(Long id, Long userId, String accountName, BigDecimal balance, String accountType, BigDecimal interestRate) {
        this.id = id;
        this.userId = userId;
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
        this.interestRate = interestRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
