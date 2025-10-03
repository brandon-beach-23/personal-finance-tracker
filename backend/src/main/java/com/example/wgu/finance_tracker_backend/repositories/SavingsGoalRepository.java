package com.example.wgu.finance_tracker_backend.repositories;

import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalResponse;
import com.example.wgu.finance_tracker_backend.models.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {

    Optional<SavingsGoal> findBySavingsAccountId(Long aLong);
}
