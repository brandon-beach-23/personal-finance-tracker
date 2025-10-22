package com.example.wgu.finance_tracker_backend.DTOs;

import com.example.wgu.finance_tracker_backend.models.SavingsAccount;

public class SavingsAccountResponse extends AccountResponse{
    private SavingsGoalResponse savingsGoalResponse;

    public SavingsGoalResponse getSavingsGoalResponse() {
        return savingsGoalResponse;
    }

    public void setSavingsGoalResponse(SavingsGoalResponse savingsGoalResponse) {
        this.savingsGoalResponse = savingsGoalResponse;
    }
}
