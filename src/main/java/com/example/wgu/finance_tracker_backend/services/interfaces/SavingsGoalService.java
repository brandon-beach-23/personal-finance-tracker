package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.models.SavingsGoal;

import java.util.List;

public interface SavingsGoalService {
    SavingsGoalService createSavingsGoal (SavingsGoal savingsGoal);
    List<SavingsGoal> getSavingsGoalByUserId(Long id);
    SavingsGoal updateSavingsGoal(SavingsGoal savingsGoal);
    void deleteSavingsGoal(Long id);
}
