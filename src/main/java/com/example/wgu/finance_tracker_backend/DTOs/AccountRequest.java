package com.example.wgu.finance_tracker_backend.DTOs;

import com.example.wgu.finance_tracker_backend.models.AccountType;

import java.math.BigDecimal;

public class AccountRequest {

    private String accountName;
    private BigDecimal balance;
    private AccountType accountType;

    public AccountRequest() {

    }

    public AccountRequest(String accountName, BigDecimal balance, AccountType accountType) {
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
