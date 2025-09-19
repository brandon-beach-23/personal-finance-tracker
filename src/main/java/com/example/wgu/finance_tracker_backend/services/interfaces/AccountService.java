package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.models.Account;

import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    Account getAccountById(Long id);
    List<Account> getAccountsByUserId(Long id);
    Account updateAccount(Account account);
    void deleteAccount(Long id);
}
