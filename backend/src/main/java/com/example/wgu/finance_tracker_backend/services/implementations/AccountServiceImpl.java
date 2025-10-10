package com.example.wgu.finance_tracker_backend.services.implementations;

import com.example.wgu.finance_tracker_backend.DTOs.AccountRequest;
import com.example.wgu.finance_tracker_backend.DTOs.AccountResponse;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.models.Account;
import com.example.wgu.finance_tracker_backend.models.AccountType;
import com.example.wgu.finance_tracker_backend.models.SavingsAccount;
import com.example.wgu.finance_tracker_backend.models.User;
import com.example.wgu.finance_tracker_backend.repositories.AccountRepository;
import com.example.wgu.finance_tracker_backend.repositories.UserRepository;
import com.example.wgu.finance_tracker_backend.services.interfaces.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest) {

        User user = userRepository.findById(accountRequest.getUserId())
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if(accountRepository.existsByAccountNameAndUser(accountRequest.getAccountName(), user)){
            throw new IllegalArgumentException("Account name already exists for user");
        }

        Account newAccount;
        if (accountRequest.getAccountType().equals("CHECKING")) {
            newAccount = new Account();
        } else {
            newAccount = new SavingsAccount();
        }

        newAccount.setAccountName(accountRequest.getAccountName());
        newAccount.setAccountType(accountRequest.getAccountType());
        newAccount.setBalance(accountRequest.getBalance());
        newAccount.setUser(user);

        Account savedAccount = accountRepository.save(newAccount);

        return convertToDTO(savedAccount);

    }



    @Override
    @Transactional
    public AccountResponse updateAccount(AccountRequest accountRequest, Long id) {
        // 1. Find existing account
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID: " + id));

        // 2. Find the User (Ownership check)
        User user = userRepository.findById(accountRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for ID: " + accountRequest.getUserId()));

        // 3. SECURITY CHECK: Ensure requesting user owns the account
        if (!existingAccount.getUser().getId().equals(user.getId())) {
            throw new SecurityException("Account does not belong to the user.");
        }

        // 4. Type Immutability Check (Cannot change account type on update)
        AccountType accountTypeEnum = accountRequest.getAccountType();
        if (accountTypeEnum == null) {
            throw new IllegalArgumentException("Account type must be provided for update validation.");
        }

        String requestType = accountTypeEnum.name();
        String existingType = existingAccount instanceof SavingsAccount ? AccountType.SAVINGS.name() : AccountType.CHECKING.name();

        if (!requestType.equals(existingType)) {
            throw new IllegalArgumentException("Account type cannot be changed during an update operation.");
        }

        // 5. Apply Updates
        existingAccount.setAccountName(accountRequest.getAccountName());
        existingAccount.setBalance(accountRequest.getBalance() != null ? accountRequest.getBalance() : existingAccount.getBalance());

        // 6. Save and convert
        Account updatedAccount = accountRepository.save(existingAccount);
        return convertToDTO(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        if(!accountRepository.existsById(id)){
            throw new ResourceNotFoundException("Account not found");
        }
        accountRepository.deleteById(id);
    }

    @Override
    public List<AccountResponse> getAccountsByUserId(Long userId) {

        return accountRepository.findAll().stream()
                .filter(account -> account.getUser() != null && account.getUser().getId().equals(userId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<AccountResponse> getAccountsByUsername(String name) {
        return List.of();
    }

    @Override
    public Optional<AccountResponse> getAccountById(Long id, String principal) {

        return accountRepository.findById(id)
                .map(this::convertToDTO);
    }

    private AccountResponse convertToDTO(Account savedAccount) {
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(savedAccount.getId());
        accountResponse.setAccountName(savedAccount.getAccountName());
        accountResponse.setBalance(savedAccount.getBalance());
        accountResponse.setAccountType(savedAccount instanceof SavingsAccount ? AccountType.SAVINGS.name() : AccountType.CHECKING.name());
        accountResponse.setUserId(savedAccount.getUser() != null ? savedAccount.getUser().getId() : null);
        return accountResponse;
    }
}
