package com.example.wgu.finance_tracker_backend.services.implementations;

import com.example.wgu.finance_tracker_backend.DTOs.AccountRequest;
import com.example.wgu.finance_tracker_backend.DTOs.AccountResponse;
import com.example.wgu.finance_tracker_backend.DTOs.SavingsAccountResponse;
import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalResponse;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.models.*;
import com.example.wgu.finance_tracker_backend.repositories.AccountRepository;
import com.example.wgu.finance_tracker_backend.repositories.SavingsGoalRepository;
import com.example.wgu.finance_tracker_backend.repositories.UserRepository;
import com.example.wgu.finance_tracker_backend.services.interfaces.AccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final SavingsGoalRepository savingsGoalRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, UserRepository userRepository, SavingsGoalRepository savingsGoalRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.savingsGoalRepository = savingsGoalRepository;
    }

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest, String username) {

        User user = userRepository.findByUserName(username)
                .orElseThrow(()-> new ResourceNotFoundException("User not found"));

        if(accountRepository.existsByAccountNameAndUser(accountRequest.getAccountName(), user)){
            throw new IllegalArgumentException("Account name already exists for user");
        }

        Account newAccount;

        String requestedType = accountRequest.getAccountType() != null ? accountRequest.getAccountType().toUpperCase() : "";
        if ("CHECKING".equals(requestedType)) {
            // Base Account instance acts as Checking Account
            newAccount = new Account();
        } else if ("SAVINGS".equals(requestedType)) {
            newAccount = new SavingsAccount();
        } else {
            // Default to a base Account if the type is unrecognized
            newAccount = new Account();
            System.out.println("Warning: Unrecognized account type, defaulting to base Account (Checking).");
        }

        newAccount.setAccountName(accountRequest.getAccountName());

        try {
            // This is the conversion that the entity's setter requires (e.g., AccountType.CHECKING)
            newAccount.setAccountType(AccountType.valueOf(requestedType));
        } catch (IllegalArgumentException e) {
            // Fallback for types that shouldn't exist based on our if/else logic, but ensures safety
            System.err.println("Critical Error: Failed to map requested type [" + requestedType + "] to AccountType enum. Setting to default CHECKING.");
            newAccount.setAccountType(AccountType.CHECKING);
        }

        newAccount.setBalance(accountRequest.getBalance());
        newAccount.setUser(user);

        System.out.println("Account Request");
        System.out.println(accountRequest.getAccountName());

        System.out.println(accountRequest.getBalance());
        System.out.println(accountRequest.getAccountType());


        Account savedAccount = accountRepository.save(newAccount);

        return convertToDTO(savedAccount);

    }

    /**
     * Updates an existing account's name and type, performing a strict ownership check.
     * @param accountRequest DTO containing new details (name, type).
     * @param accountId The ID of the account to update.
     * @param username The username of the authenticated user.
     * @return The updated AccountResponse DTO.
     */
    @Transactional
    public AccountResponse updateAccount(AccountRequest accountRequest, Long accountId, String username) {

        // 1. Fetch User
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated User not found with username: " + username));

        // 2. Fetch Account
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        // 3. Ownership Check (CRITICAL SECURITY STEP)
        if (!existingAccount.getUser().getUserName().equals(username)) {
            throw new IllegalArgumentException("Account ID " + accountId + " does not belong to user " + username);
        }

        // 4. Apply updates
        existingAccount.setAccountName(accountRequest.getAccountName());

        // Normalize and convert AccountType (same logic as create)
        String requestedType = accountRequest.getAccountType() != null ? accountRequest.getAccountType().toUpperCase() : "";

        try {
            existingAccount.setAccountType(AccountType.valueOf(requestedType));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account type: " + requestedType);
        }

        // NOTE: Balance is intentionally NOT updated here, per user requirement.

        Account updatedAccount = accountRepository.save(existingAccount);
        return convertToDTO(updatedAccount);
    }

    /**
     * Deletes an account, performing a strict ownership check.
     * @param accountId The ID of the account to delete.
     * @param username The username of the authenticated user.
     */
    @Transactional
    public void deleteAccount(Long accountId, String username) {

        // 1. Fetch User (implicitly checks if user exists)
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated User not found with username: " + username));

        // 2. Fetch Account
        Account accountToDelete = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with ID: " + accountId));

        // 3. Ownership Check (CRITICAL SECURITY STEP)
        if (!accountToDelete.getUser().getUserName().equals(username)) {
            throw new IllegalArgumentException("Account ID " + accountId + " does not belong to user " + username);
        }

        // 4. Delete with Exception Handling
        try {
            accountRepository.delete(accountToDelete);
        } catch (DataIntegrityViolationException e) {
            // ⭐️ FIX: Catch the database error (likely caused by foreign key constraints)
            // and throw a more descriptive error that should map to a 409 Conflict or 400 Bad Request.

            // This exception should be caught by your global exception handler and mapped to a 409 or 400.
            // It's descriptive for the frontend.
            throw new IllegalStateException("Cannot delete account ID " + accountId +
                    " because it has associated financial data (transactions, goals, etc.). " +
                    "Please delete all linked data first.", e);
        }
    }

    @Override
    public List<AccountResponse> getAccountsByUserId(Long userId) {

        return accountRepository.findAll().stream()
                .filter(account -> account.getUser() != null && account.getUser().getId().equals(userId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public List<AccountResponse> getAccountsByUsername(String username) {
        User user = userRepository.findByUserName(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Account> accounts = accountRepository.findByUserUserName(username);

        return accounts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single account by ID, ensuring ownership by the authenticated user.
     * This supports the GET /api/accounts/{id} endpoint.
     * @param accountId The ID of the account to retrieve.
     * @param username The username of the authenticated user.
     * @return An Optional containing the AccountResponse DTO.
     */

    public Optional<AccountResponse> getAccountById(Long accountId, String username) {
        // 1. Fetch the Account
        return accountRepository.findById(accountId)
                .map(account -> {
                    // 2. Ownership Check (CRITICAL)
                    if (!account.getUser().getUserName().equals(username)) {
                        // Throw a security exception if unauthorized access is attempted
                        throw new IllegalArgumentException("Account ID " + accountId + " does not belong to user " + username);
                    }
                    // 3. Convert and return
                    return convertToDTO(account);
                });
    }

    private AccountResponse convertToDTO(Account savedAccount) {
        if (savedAccount instanceof SavingsAccount) {
            SavingsAccountResponse savingsResponse = new SavingsAccountResponse();
            savingsResponse.setId(savedAccount.getId());
            savingsResponse.setAccountName(savedAccount.getAccountName());
            savingsResponse.setBalance(savedAccount.getBalance());
            savingsResponse.setUserId(savedAccount.getUser().getId());
            savingsResponse.setAccountType("SAVINGS");

            // Attach savings goal if present

            Optional<SavingsGoal> goalOpt = savingsGoalRepository.findBySavingsAccountId(savedAccount.getId());
            goalOpt.ifPresent(goal -> {
                SavingsGoalResponse goalResponse = new SavingsGoalResponse();
                goalResponse.setId(goal.getId());
                goalResponse.setGoalName(goal.getGoalName());
                goalResponse.setTargetAmount(goal.getTargetAmount());
                goalResponse.setSavingsAccountId(goal.getSavingsAccount().getId());

                savingsResponse.setSavingsGoalResponse(goalResponse);
            });
            return savingsResponse;
        } else {
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setId(savedAccount.getId());
            accountResponse.setAccountName(savedAccount.getAccountName());
            accountResponse.setBalance(savedAccount.getBalance());
            accountResponse.setUserId(savedAccount.getUser().getId());
            accountResponse.setAccountType("CHECKING");
            return accountResponse;
        }
    }

}
