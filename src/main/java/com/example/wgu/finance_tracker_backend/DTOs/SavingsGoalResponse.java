package com.example.wgu.finance_tracker_backend.DTOs;

import java.math.BigDecimal;

public class SavingsGoalResponse {

    private Long id;
    private String goalName;
    private BigDecimal targetAmount;
    private Long accountId;
    private Long userId;

    public SavingsGoalResponse() {
    }

    public SavingsGoalResponse(Long id, String goalName, BigDecimal targetAmount, Long accountId, Long userId) {
        this.id = id;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.accountId = accountId;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
