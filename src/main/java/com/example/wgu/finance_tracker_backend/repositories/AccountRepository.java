package com.example.wgu.finance_tracker_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountRepository, Long> {
    Optional<AccountRepository> findByUserName(String username);
}
