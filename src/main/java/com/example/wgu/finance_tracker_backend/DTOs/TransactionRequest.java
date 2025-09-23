package com.example.wgu.finance_tracker_backend.DTOs;

public class TransactionRequest {

    private String name;
    private double amount;
    private String categoryName;
    private String transactionType;
    private Long accountId;

    public TransactionRequest() {
    }

    public TransactionRequest(String name, double amount, String categoryName, String transactionType, Long accountId) {
        this.name = name;
        this.amount = amount;
        this.categoryName = categoryName;
        this.transactionType = transactionType;
        this.accountId = accountId;
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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
}
