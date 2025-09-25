package com.example.wgu.finance_tracker_backend.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionRequest {

    private String name;
    private BigDecimal amount;
    private String categoryName;
    private String transactionType;
    private Long accountId;
    private LocalDate transactionDate;

    public TransactionRequest() {
    }

    public TransactionRequest(String name, BigDecimal amount, String categoryName, String transactionType, Long accountId, LocalDate transactionDate) {
        this.name = name;
        this.amount = amount;
        this.categoryName = categoryName;
        this.transactionType = transactionType;
        this.accountId = accountId;
        this.transactionDate = transactionDate;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }
}
