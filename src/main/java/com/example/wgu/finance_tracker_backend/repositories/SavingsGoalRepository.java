package com.example.wgu.finance_tracker_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoalRepository, Long> {
    @Override
    Optional<SavingsGoalRepository> findById(Long id);
}
