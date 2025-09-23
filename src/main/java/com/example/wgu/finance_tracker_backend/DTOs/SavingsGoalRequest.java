package com.example.wgu.finance_tracker_backend.DTOs;

import java.math.BigDecimal;

public class SavingsGoalRequest {
    private String goalName;
    private BigDecimal targetAmount;
    private Long accountId;

    public SavingsGoalRequest() {
    }

    public SavingsGoalRequest(String goalName, BigDecimal targetAmount, Long accountId) {
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.accountId = accountId;
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
}
