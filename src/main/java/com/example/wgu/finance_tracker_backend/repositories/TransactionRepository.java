package com.example.wgu.finance_tracker_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<TransactionRepository, Long> {
    @Override
    Optional<TransactionRepository> findById(Long id);
}
