package com.example.wgu.finance_tracker_backend.unit_tests;

import com.example.wgu.finance_tracker_backend.DTOs.CategoryResponse;
import com.example.wgu.finance_tracker_backend.DTOs.TransactionRequest;
import com.example.wgu.finance_tracker_backend.DTOs.TransactionResponse;
import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
import com.example.wgu.finance_tracker_backend.models.*;
import com.example.wgu.finance_tracker_backend.repositories.AccountRepository;
import com.example.wgu.finance_tracker_backend.repositories.CategoryRepository;
import com.example.wgu.finance_tracker_backend.repositories.TransactionRepository;
import com.example.wgu.finance_tracker_backend.services.implementations.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CategoryRepository categoryRepository;


    @InjectMocks
    private TransactionServiceImpl transactionService;

    private static final Long TEST_TRANSACTION_ID = 1L;
    private static final Long TEST_ACCOUNT_ID = 1L;
    private static final String TEST_CATEGORY_NAME = "Groceries";
    private static final BigDecimal TEST_TRANSACTION_AMOUNT = BigDecimal.valueOf(50);
    private static final BigDecimal TEST_STARTING_BALANCE = BigDecimal.valueOf(100);
    private static final BigDecimal EXPECTED_CREDIT_BALANCE = BigDecimal.valueOf(150);
    private static final BigDecimal EXPECTED_DEBIT_BALANCE = BigDecimal.valueOf(50);
    private static final LocalDate TEST_DATE = LocalDate.of(2020, 1, 1);

    private Account mockAccount;
    private Category mockCategory;
    private Transaction mockTransaction;
    private TransactionRequest mockTransactionRequest;

    @BeforeEach
    void setUp() {
        mockAccount = new Account();
        mockAccount.setId(TEST_ACCOUNT_ID);
        mockAccount.setBalance(TEST_STARTING_BALANCE);

        User mockUser = new User();
        mockUser.setUserName("testuser");
        mockAccount.setUser(mockUser);

        mockCategory = new Category();
        mockCategory.setId(10L);
        mockCategory.setName(TEST_CATEGORY_NAME);

        mockTransaction = new Transaction();
        mockTransaction.setTransactionId(1L);
        mockTransaction.setAmount(TEST_TRANSACTION_AMOUNT);
        mockTransaction.setCategory(mockCategory);
        mockTransaction.setDate(TEST_DATE);

        Mockito.reset(transactionRepository, accountRepository, categoryRepository);
    }

    @Test
    void createDebitTransaction_Success() {
        mockAccount.setId(TEST_ACCOUNT_ID);
        mockAccount.setBalance(TEST_STARTING_BALANCE);

        mockCategory.setName(TEST_CATEGORY_NAME);

        TransactionRequest debitRequest = new TransactionRequest();
        debitRequest.setTransactionName("Coffee Shop");
        debitRequest.setTransactionAmount(TEST_TRANSACTION_AMOUNT);
        debitRequest.setCategoryId(mockCategory.getId());
        debitRequest.setTransactionType(TransactionType.DEBIT.toString());
        debitRequest.setAccountId(TEST_ACCOUNT_ID);
        debitRequest.setTransactionDate(TEST_DATE);

        Transaction savedTransaction = new Transaction();
        savedTransaction.setTransactionId(TEST_TRANSACTION_ID);
        savedTransaction.setAccount(mockAccount);
        savedTransaction.setCategory(mockCategory);
        savedTransaction.setDate(TEST_DATE);
        savedTransaction.setAmount(TEST_TRANSACTION_AMOUNT);
        savedTransaction.setTransactionType(TransactionType.DEBIT);
        savedTransaction.setName("Coffee Shop");

        Mockito.when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(mockAccount));
        Mockito.when(categoryRepository.findById(mockCategory.getId())).thenReturn(Optional.of(mockCategory));

        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(savedTransaction);

        transactionService.createTransaction(debitRequest, "testuser");
        assertEquals(EXPECTED_DEBIT_BALANCE, mockAccount.getBalance(), "Account balance should be debited by 50.00");

        Mockito.verify(accountRepository, Mockito.times(1)).save(mockAccount);
        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
    }
}