package com.example.wgu.finance_tracker_backend.DTOs;

import java.math.BigDecimal;

public class TransactionResponse {

    private Long id;
    private String name;
    private BigDecimal amount;
    private String categoryName;
    private String transactionType;
    private Long accountId;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, String name, BigDecimal amount, String categoryName, String transactionType, Long accountId) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.categoryName = categoryName;
        this.transactionType = transactionType;
        this.accountId = accountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
