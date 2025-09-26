package com.example.wgu.finance_tracker_backend.services.implementations;

import com.example.wgu.finance_tracker_backend.DTOs.TransactionRequest;
import com.example.wgu.finance_tracker_backend.DTOs.TransactionResponse;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.models.Account;
import com.example.wgu.finance_tracker_backend.models.Category;
import com.example.wgu.finance_tracker_backend.models.Transaction;
import com.example.wgu.finance_tracker_backend.models.TransactionType;
import com.example.wgu.finance_tracker_backend.repositories.AccountRepository;
import com.example.wgu.finance_tracker_backend.repositories.CategoryRepository;
import com.example.wgu.finance_tracker_backend.repositories.TransactionRepository;
import com.example.wgu.finance_tracker_backend.services.interfaces.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;


    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, CategoryRepository categoryRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public TransactionResponse create(TransactionRequest transactionRequest) {

        //Create the account and category objects required for the transaction entity
        Account account = accountRepository.findById(transactionRequest.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        Category category = categoryRepository.findByName(transactionRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        //Convert the string transactionType from the request DTO to an Enum for the transaction entity
        TransactionType transactionType;
        try {
            transactionType = TransactionType.valueOf(transactionRequest.getTransactionType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionRequest.getTransactionType());
        }

        //Determine if the transaction will debit or credit the account
        //Then save the account with the updated balance
        if (transactionType == TransactionType.DEBIT) {
            account.debit(transactionRequest.getAmount());
        }
        else if (transactionType == TransactionType.CREDIT) {
            account.credit(transactionRequest.getAmount());
        } else {
            throw new IllegalArgumentException("Invalid transaction type" + transactionRequest.getTransactionType());
        }
        accountRepository.save(account);

        //Create and set the fields of the new transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setTransactionType(transactionType);
        transaction.setName(transactionRequest.getName());
        transaction.setDate(transactionRequest.getTransactionDate());

        //Create a new transaction object of the saved transaction because the id is needed
        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToDto(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse update(Long transactionId, TransactionRequest transactionRequest) {

        //Find the existing transaction
        Transaction existingTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        //Make sure the transaction belongs to the account
        if(!existingTransaction.getAccount().getId().equals(transactionRequest.getAccountId())) {
            throw new IllegalArgumentException("Transaction does not belong to the specified account");
        }

        //Find the account and category entities
        Account account = existingTransaction.getAccount();
        Category newCategory = categoryRepository.findByName(transactionRequest.getCategoryName())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        //Reverse the old transaction
        if (existingTransaction.getTransactionType() == TransactionType.DEBIT) {
            account.credit(existingTransaction.getAmount());
        }
        else if (existingTransaction.getTransactionType() == TransactionType.CREDIT) {
            account.debit(existingTransaction.getAmount());
        }

        //Convert the string transactionType from the request DTO to an Enum for the transaction entity
        TransactionType newTransactionType;
        try {
            newTransactionType = TransactionType.valueOf(transactionRequest.getTransactionType());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid transaction type: " + transactionRequest.getTransactionType());
        }

        //Apply  the new transaction
        if (newTransactionType == TransactionType.DEBIT) {
            account.debit(transactionRequest.getAmount());
        } else if (newTransactionType == TransactionType.CREDIT) {
            account.credit(transactionRequest.getAmount());
        }

        //Update transaction with new values
        existingTransaction.setCategory(newCategory);
        existingTransaction.setAmount(transactionRequest.getAmount());
        existingTransaction.setTransactionType(newTransactionType);
        existingTransaction.setName(transactionRequest.getName());
        existingTransaction.setDate(transactionRequest.getTransactionDate());

        //Save the account and transaction
        accountRepository.save(account);
        Transaction updatedTransaction = transactionRepository.save(existingTransaction);

        return convertToDto(updatedTransaction);
    }

    @Override
    @Transactional
    public void delete(Long transactionId) {
        Transaction exisitngTransaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));

        Account account = exisitngTransaction.getAccount();

        if(exisitngTransaction.getTransactionType() == TransactionType.DEBIT) {
            account.credit(exisitngTransaction.getAmount());
        }
        else if (exisitngTransaction.getTransactionType() == TransactionType.CREDIT) {
            account.debit(exisitngTransaction.getAmount());
        }
        transactionRepository.delete(exisitngTransaction);
        accountRepository.save(account);
    }

    @Override
    public Optional<TransactionResponse> getById(Long transactionId) {
        return transactionRepository.findById(transactionId).map(this::convertToDto);
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccountIdAndName(Long accountId, String name) {
        return List.of();
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccountIdAndCategory(Long accountId, Category category) {
        return List.of();
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccountIdAndAmount(Long accountId, BigDecimal amount) {
        return List.of();
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccountIdAndTransactionType(Long accountId, TransactionType transactionType) {
        return List.of();
    }

    @Override
    public BigDecimal getSumAmountByAccountIdAndTransactionType(Long accountid, TransactionType transactionType) {
        return null;
    }

    private TransactionResponse convertToDto(Transaction transaction) {
        TransactionResponse response = new TransactionResponse();
        response.setId(transaction.getId());
        response.setAccountId(transaction.getAccount().getId());
        response.setCategoryName(transaction.getCategory().getName());
        response.setAmount(transaction.getAmount());
        response.setTransactionType(transaction.getTransactionType().toString());
        response.setName(transaction.getName());
        response.setTransactionDate(transaction.getDate());
        return response;
    }
}
