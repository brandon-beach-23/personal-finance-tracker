package com.example.wgu.finance_tracker_backend.services.implementations;

import com.example.wgu.finance_tracker_backend.DTOs.AccountRequest;
import com.example.wgu.finance_tracker_backend.DTOs.AccountResponse;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.models.Account;
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
            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setInterestRate(accountRequest.getInterestRate());
            newAccount = savingsAccount;
        }

        newAccount.setAccountName(accountRequest.getAccountName());
        newAccount.setAccountType(accountRequest.getAccountType());
        newAccount.setBalance(accountRequest.getBalance());
        newAccount.setUser(user);

        Account savedAccount = accountRepository.save(newAccount);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(savedAccount.getId());
        accountResponse.setAccountName(savedAccount.getAccountName());
        accountResponse.setBalance(savedAccount.getBalance());
        accountResponse.setUserId(savedAccount.getUser().getId());
        accountResponse.setAccountType(savedAccount.getAccountType().toString());

        return accountResponse;
    }

    @Override
    @Transactional
    public AccountResponse updateAccount(AccountRequest accountRequest, Long id) {

        // Step 1: Find the existing account by its ID and a specific user.
        User user = userRepository.findById(accountRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found."));

        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found."));

        // Step 2: Ensure the user owns this account for security.
        if (!existingAccount.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized: User does not own this account.");
        }

        // Step 3: Update common properties from the request DTO.
        existingAccount.setAccountName(accountRequest.getAccountName());
        existingAccount.setBalance(accountRequest.getBalance());
        existingAccount.setAccountType(accountRequest.getAccountType());

        // Step 4: Handle specific fields for Savings Accounts.
        if (existingAccount instanceof SavingsAccount) {
            ((SavingsAccount) existingAccount).setInterestRate(accountRequest.getInterestRate());
        }

        // Step 5: Save the updated account and map to DTO.
        Account updatedAccount = accountRepository.save(existingAccount);

        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setId(updatedAccount.getId());
        accountResponse.setAccountName(updatedAccount.getAccountName());
        accountResponse.setBalance(updatedAccount.getBalance());
        accountResponse.setUserId(updatedAccount.getUser().getId());
        accountResponse.setAccountType(updatedAccount.getAccountType().toString());

        // Set interest rate for Savings Accounts on the response DTO
        if (updatedAccount instanceof SavingsAccount) {
            accountResponse.setInterestRate(((SavingsAccount) updatedAccount).getInterestRate());
        }

        return accountResponse;
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
        // Step 1: Find the user to ensure they exist.
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));

        // Step 2: Use the custom repository method to find all accounts for the user.
        List<Account> accounts = accountRepository.findByUser(user);

        // Step 3: Map the list of Account entities to a list of AccountResponse DTOs.
        return accounts.stream()
                .map(account -> {
                    AccountResponse dto = new AccountResponse();
                    dto.setId(account.getId());
                    dto.setAccountName(account.getAccountName());
                    dto.setBalance(account.getBalance());
                    dto.setUserId(account.getUser().getId());
                    dto.setAccountType(account.getAccountType().toString());
                    // Check for SavingAccount to include interest rate
                    if (account instanceof SavingsAccount) {
                        dto.setInterestRate(((SavingsAccount) account).getInterestRate());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

    }


    @Override
    public Optional<AccountResponse> getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountName(account.getAccountName());
        accountResponse.setBalance(account.getBalance());
        accountResponse.setAccountType(account.getAccountType().toString());
        accountResponse.setUserId(account.getUser().getId());
        return Optional.of(accountResponse);
    }
}
