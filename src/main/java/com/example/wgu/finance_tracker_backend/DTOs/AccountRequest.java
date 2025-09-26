package com.example.wgu.finance_tracker_backend.DTOs;

import com.example.wgu.finance_tracker_backend.models.AccountType;
import com.example.wgu.finance_tracker_backend.models.User;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountRequest {

    private String accountName;
    private BigDecimal balance;
    private AccountType accountType;
    private Long userId;


    public AccountRequest() {

    }

    public AccountRequest(String accountName, BigDecimal balance, AccountType accountType, Long userId) {
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
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

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getUserId() {
        return userId;
    }

   public void setUserId(Long userId) {
        this.userId = userId;
    }

}
