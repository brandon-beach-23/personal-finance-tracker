package com.example.wgu.finance_tracker_backend.services.interfaces;

import com.example.wgu.finance_tracker_backend.models.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    List <Transaction> getTransactionsByAccountId(Long id);
    Transaction updateTransaction(Transaction transaction);
    void deleteTransaction(Long id);

}
