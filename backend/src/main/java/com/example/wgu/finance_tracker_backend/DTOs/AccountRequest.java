package com.example.wgu.finance_tracker_backend.DTOs;

import com.example.wgu.finance_tracker_backend.models.AccountType;
import com.example.wgu.finance_tracker_backend.models.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountRequest {

    private String accountName;
    @JsonProperty("accountBalance")
    private BigDecimal balance;
    private String accountType;
    private Long userId;


    public AccountRequest() {

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

    public Long getUserId() {
        return userId;
    }

   public void setUserId(Long userId) {
        this.userId = userId;
    }

}
