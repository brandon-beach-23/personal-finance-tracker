package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.DTOs.TransactionRequest;
import com.example.wgu.finance_tracker_backend.DTOs.TransactionResponse;
import com.example.wgu.finance_tracker_backend.models.Category;
import com.example.wgu.finance_tracker_backend.models.TransactionType;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionService {

    //Basic CRUD Operations
    TransactionResponse createTransaction(TransactionRequest transactionRequest, String username);
    TransactionResponse updateTransaction(Long transactionId, TransactionRequest transactionRequest, String username);
    void deleteTransaction(Long transactionId, String username);
    Optional<TransactionResponse> getById(Long transactionId);
    List<TransactionResponse> getTransactionsByAccountId(Long accountId, String username);

    //Reporting and Searching - implemented at a later date
    List<TransactionResponse> getTransactionsByAccountIdAndName(Long accountId, String name);
    List<TransactionResponse> getTransactionsByAccountIdAndCategory(Long accountId, Category category);
    List<TransactionResponse> getTransactionsByAccountIdAndAmount(Long accountId, BigDecimal amount);
    List<TransactionResponse> getTransactionsByAccountIdAndTransactionType(Long accountId, TransactionType transactionType);
    BigDecimal getSumAmountByAccountIdAndTransactionType(Long accountid, TransactionType transactionType);

}
