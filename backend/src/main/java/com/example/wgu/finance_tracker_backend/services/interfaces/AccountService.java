package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.DTOs.AccountRequest;
import com.example.wgu.finance_tracker_backend.DTOs.AccountResponse;
import com.example.wgu.finance_tracker_backend.models.Account;
import com.example.wgu.finance_tracker_backend.models.User;
import com.example.wgu.finance_tracker_backend.repositories.AccountRepository;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    AccountResponse createAccount(AccountRequest accountRequest, String username);
    AccountResponse updateAccount(AccountRequest accountRequest, Long accountId, String username);
    void deleteAccount(Long accountId, String username);
    List<AccountResponse> getAccountsByUserId(Long id);
    Optional<AccountResponse> getAccountById(Long id, String principal);

    List<AccountResponse> getAccountsByUsername(String name);
}
