package com.example.wgu.finance_tracker_backend.DTOs;

import java.math.BigDecimal;

public class SavingsGoalResponse {

    private Long id;
    private String goalName;
    private BigDecimal targetAmount;
    private Long savingsAccountId;
    private Long userId;

    // Constructors, Getters, and Setters
    public SavingsGoalResponse() {
    }

    public SavingsGoalResponse(Long id, String goalName, BigDecimal targetAmount, Long savingsAccountId, Long userId) {
        this.id = id;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.savingsAccountId = savingsAccountId;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Long getSavingsAccountId() {
        return savingsAccountId;
    }

    public void setSavingsAccountId(Long savingsAccountId) {
        this.savingsAccountId = savingsAccountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
