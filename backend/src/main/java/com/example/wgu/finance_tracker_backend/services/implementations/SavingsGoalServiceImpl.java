package com.example.wgu.finance_tracker_backend.services.implementations;

import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalRequest;
import com.example.wgu.finance_tracker_backend.DTOs.SavingsGoalResponse;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.models.Account;
import com.example.wgu.finance_tracker_backend.models.SavingsAccount;
import com.example.wgu.finance_tracker_backend.models.SavingsGoal;
import com.example.wgu.finance_tracker_backend.models.User;
import com.example.wgu.finance_tracker_backend.repositories.AccountRepository;
import com.example.wgu.finance_tracker_backend.repositories.SavingsGoalRepository;
import com.example.wgu.finance_tracker_backend.repositories.UserRepository;
import com.example.wgu.finance_tracker_backend.services.interfaces.SavingsGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Optional;

@Service
public class SavingsGoalServiceImpl implements SavingsGoalService {
    private final SavingsGoalRepository savingsGoalRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Autowired
    public SavingsGoalServiceImpl(SavingsGoalRepository savingsGoalRepository, AccountRepository accountRepository, UserRepository userRepository) {
        this.savingsGoalRepository = savingsGoalRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public SavingsGoalResponse createSavingsGoal(SavingsGoalRequest savingsGoalRequest, Principal principal) {

        //Check if the account exists from the account id
        Account account = accountRepository.findById(savingsGoalRequest.getSavingsAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));

        //Check if the account is a savings account
        if (!(account instanceof SavingsAccount)) {
            throw new IllegalArgumentException("Goal must be created on SavingsAccount");
        }

        //Check if the user exists
        String username = principal.getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated User Not Found"));

        //Check if the user owns the account
        if (!account.getUser().getUserName().equals(username)) {
            throw new IllegalArgumentException("User is not owner of SavingsAccount");
        }

        //Checks if the savings account already has a goal
        Optional<SavingsGoal> existingSavingsGoal = savingsGoalRepository.findBySavingsAccountId(savingsGoalRequest.getSavingsAccountId());
        if (existingSavingsGoal.isPresent()) {
            throw new IllegalArgumentException("Savings Goal already exists");
        }

        //Create a new savings goal
        SavingsGoal savingsGoal = new SavingsGoal();

        savingsGoal.setSavingsAccount((SavingsAccount) account);
        savingsGoal.setGoalName(savingsGoalRequest.getGoalName());
        savingsGoal.setTargetAmount(savingsGoalRequest.getTargetAmount());
        SavingsGoal savedSavingsGoal = savingsGoalRepository.save(savingsGoal);

        return convertToDTO(savedSavingsGoal);
    }

    @Override
    @Transactional
    public SavingsGoalResponse updateSavingsGoal(SavingsGoalRequest savingsGoalRequest, Long id, Principal principal) {
        //Check if the account exists from the account id
        Account account = accountRepository.findById(savingsGoalRequest.getSavingsAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));

        //Check if the account is a savings account
        if (!(account instanceof SavingsAccount)) {
            throw new IllegalArgumentException("Goal must be created on SavingsAccount");
        }

        //Check if the user exists
        String username = principal.getName();
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new ResourceNotFoundException("Authorized User Not Found"));

        //Check if the user owns the account
        if (!account.getUser().getUserName().equals(username)) {
            throw new IllegalArgumentException("User is not owner of SavingsAccount");
        }

        //Check if the savings goal exists
        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found."));

        if (!savingsGoal.getSavingsAccount().getUser().getUserName().equals(username)) {
            throw new IllegalArgumentException("User is not owner of SavingsGoal");
        }

        savingsGoal.setUser(user);
        savingsGoal.setSavingsAccount((SavingsAccount) account);
        savingsGoal.setGoalName(savingsGoalRequest.getGoalName());
        savingsGoal.setTargetAmount(savingsGoalRequest.getTargetAmount());
        SavingsGoal savedSavingsGoal = savingsGoalRepository.save(savingsGoal);

        return convertToDTO(savedSavingsGoal);
    }

    @Override
    @Transactional
    public void deleteSavingsGoal(Long id, Principal principal) {

        String username = principal.getName();
        //Check if the savings goal exists
        SavingsGoal savingsGoal = savingsGoalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Savings goal not found."));

        if (!savingsGoal.getSavingsAccount().getUser().getUserName().equals(username)) {
            throw new IllegalArgumentException("User is not owner of SavingsGoal");
        }

        savingsGoalRepository.delete(savingsGoal);
    }

    @Override
    public Optional<SavingsGoalResponse> getById(Long id) {
        return savingsGoalRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Override
    public Optional<SavingsGoalResponse> getBySavingsAccountId(Long savingsAccountId) {
        return savingsGoalRepository.findBySavingsAccountId(savingsAccountId)
                .map(this::convertToDTO);
    }

    public SavingsGoalResponse convertToDTO(SavingsGoal savingsGoal) {
        SavingsGoalResponse savingsGoalResponse = new SavingsGoalResponse();
        savingsGoalResponse.setId(savingsGoal.getId());
        savingsGoalResponse.setSavingsAccountId(savingsGoal.getSavingsAccount().getId());
        savingsGoalResponse.setGoalName(savingsGoal.getGoalName());
        savingsGoalResponse.setTargetAmount(savingsGoal.getTargetAmount());

        return savingsGoalResponse;
    }

}
