package com.example.wgu.finance_tracker_backend.repositories;

import com.example.wgu.finance_tracker_backend.models.Account;
import com.example.wgu.finance_tracker_backend.models.AccountType;
import com.example.wgu.finance_tracker_backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserUserName(String username);
    Boolean existsByAccountNameAndUser(String accountName, User user);
    List<Account> findByAccountTypeAndUser(AccountType accountType, User user);
}
