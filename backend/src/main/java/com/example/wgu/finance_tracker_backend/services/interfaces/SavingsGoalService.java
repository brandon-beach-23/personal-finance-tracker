package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalRequest;
import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalResponse;
import com.example.wgu.finance_tracker_backend.models.SavingsGoal;

import java.util.List;
import java.util.Optional;

public interface SavingsGoalService {
    SavingsGoalResponse createSavingsGoal (SavingsGoalRequest savingsGoalRequest);
    SavingsGoalResponse updateSavingsGoal(SavingsGoalRequest savingsGoalRequest, Long id);
    void deleteSavingsGoal(Long id);
    Optional<SavingsGoalResponse> getById(Long id);
    Optional<SavingsGoalResponse> getBySavingsAccountId(Long savingsAccountId);
}
