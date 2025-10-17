//package com.example.wgu.finance_tracker_backend.unit_tests;
//
//import com.example.wgu.finance_tracker_backend.DTOs.CategoryResponse;
//import com.example.wgu.finance_tracker_backend.DTOs.TransactionRequest;
//import com.example.wgu.finance_tracker_backend.DTOs.TransactionResponse;
//import com.example.wgu.finance_tracker_backend.exceptions.ResourceNotFoundException;
//import com.example.wgu.finance_tracker_backend.models.*;
//import com.example.wgu.finance_tracker_backend.repositories.AccountRepository;
//import com.example.wgu.finance_tracker_backend.repositories.CategoryRepository;
//import com.example.wgu.finance_tracker_backend.repositories.TransactionRepository;
//import com.example.wgu.finance_tracker_backend.services.implementations.TransactionServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class TransactionServiceImplTest {
//
//    @Mock
//    private TransactionRepository transactionRepository;
//    @Mock
//    private AccountRepository accountRepository;
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private TransactionServiceImpl transactionService;
//
//    private static final Long TEST_TRANSACTION_ID = 1L;
//    private static final Long TEST_ACCOUNT_ID = 1L;
//    private static final String TEST_CATEGORY_NAME = "Groceries";
//    private static final BigDecimal TEST_TRANSACTION_AMOUNT= BigDecimal.valueOf(50);
//    private static final BigDecimal TEST_STARTING_BALANCE = BigDecimal.valueOf(100);
//    private static final BigDecimal EXPECTED_CREDIT_BALANCE = BigDecimal.valueOf(150);
//    private static final BigDecimal EXPECTED_DEBIT_BALANCE = BigDecimal.valueOf(50);
//    private static final LocalDate TEST_DATE = LocalDate.of(2020, 1, 1);
//
//    private Account mockAccount;
//    private Category mockCategory;
//    private Transaction mockTransaction;
//    private TransactionRequest mockTransactionRequest;
//
//    @BeforeEach
//    void setUp() {
//        mockAccount = new Account();
//        mockAccount.setId(TEST_ACCOUNT_ID);
//        mockAccount.setBalance(TEST_STARTING_BALANCE);
//
//        mockCategory = new Category();
//        mockCategory.setId(10L);
//        mockCategory.setName(TEST_CATEGORY_NAME);
//
//        mockTransaction = new Transaction();
//        mockTransaction.setId(1L);
//        mockTransaction.setAmount(TEST_TRANSACTION_AMOUNT);
//        mockTransaction.setCategory(mockCategory);
//        mockTransaction.setDate(TEST_DATE);
//
//        Mockito.reset(transactionRepository, accountRepository, categoryRepository);
//
//    }
//
//    @Test
//    void createDebitTransaction_Success() {
//        mockAccount.setId(TEST_ACCOUNT_ID);
//        mockAccount.setBalance(TEST_STARTING_BALANCE);
//
//        mockCategory.setName(TEST_CATEGORY_NAME);
//
//        TransactionRequest debitRequest = new TransactionRequest(
//                "Coffee Shop",
//                TEST_TRANSACTION_AMOUNT,
//                TEST_CATEGORY_NAME,
//                TransactionType.DEBIT.toString(),
//                TEST_ACCOUNT_ID,
//                TEST_DATE
//        );
//
//        Transaction savedTransaction = new Transaction();
//        savedTransaction.setId(TEST_TRANSACTION_ID);
//        savedTransaction.setAccount(mockAccount);
//        savedTransaction.setCategory(mockCategory);
//        savedTransaction.setDate(TEST_DATE);
//        savedTransaction.setAmount(TEST_TRANSACTION_AMOUNT);
//        savedTransaction.setTransactionType(TransactionType.DEBIT);
//        savedTransaction.setName("Coffee Shop");
//
//        Mockito.when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(mockAccount));
//        Mockito.when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);
//        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(savedTransaction);
//
//        transactionService.createTransaction(debitRequest);
//
//        // Assert
//        // 2. Verify the side effects (Account balance update)
//        assertEquals(EXPECTED_DEBIT_BALANCE, mockAccount.getBalance(), "Account balance should be debited by 50.00");
//
//        // 3. Verify interactions with repositories
//        Mockito.verify(accountRepository, Mockito.times(1)).save(mockAccount);
//        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
//    }
//
//    @Test
//    void createCreditTransaction_Success() {
//        // Arrange
//        mockAccount.setId(TEST_ACCOUNT_ID);
//        mockAccount.setBalance(TEST_STARTING_BALANCE);
//
//        mockCategory.setName(TEST_CATEGORY_NAME);
//
//        TransactionRequest creditRequest = new TransactionRequest(
//                "Coffee Shop",
//                TEST_TRANSACTION_AMOUNT,
//                TEST_CATEGORY_NAME,
//                TransactionType.CREDIT.toString(),
//                TEST_ACCOUNT_ID,
//                TEST_DATE
//        );
//
//        Transaction savedTransaction = new Transaction();
//        savedTransaction.setId(TEST_TRANSACTION_ID);
//        savedTransaction.setAccount(mockAccount);
//        savedTransaction.setCategory(mockCategory);
//        savedTransaction.setDate(TEST_DATE);
//        savedTransaction.setAmount(TEST_TRANSACTION_AMOUNT);
//        savedTransaction.setTransactionType(TransactionType.CREDIT);
//        savedTransaction.setName("Lottery winnings");
//
//        Mockito.when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(mockAccount));
//        Mockito.when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);
//        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(savedTransaction);
//
//        transactionService.createTransaction(creditRequest);
//
//        // Assert
//        // 2. Verify the side effects (Account balance update)
//        assertEquals(EXPECTED_CREDIT_BALANCE, mockAccount.getBalance(), "Account balance should be debited by 50.00");
//
//        // 3. Verify interactions with repositories
//        Mockito.verify(accountRepository, Mockito.times(1)).save(mockAccount);
//        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
//    }
//
//    @Test
//    void create_InvalidTransactionType_ThrowsException() {
//        // Arrange
//        TransactionRequest invalidRequest = new TransactionRequest(
//                "Bad Spend",
//                TEST_TRANSACTION_AMOUNT,
//                TEST_CATEGORY_NAME,
//                "SPEND", // Invalid type (not DEBIT or CREDIT)
//                TEST_ACCOUNT_ID,
//                TEST_DATE
//        );
//
//        Mockito.when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(mockAccount));
//        Mockito.when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//
//        // Act & Assert
//        // Expect that calling the method throws an IllegalArgumentException
//        assertThrows(IllegalArgumentException.class, () ->
//                        transactionService.createTransaction(invalidRequest),
//                "Should throw IllegalArgumentException for invalid type"
//        );
//
//        // Assert 2: Verify that no changes were saved to the database
//        // The balance must not be changed, and no transaction should be saved.
//        assertEquals(TEST_STARTING_BALANCE, mockAccount.getBalance(), "Account balance should remain unchanged after failure.");
//        Mockito.verify(transactionRepository, Mockito.times(0)).save(Mockito.any(Transaction.class));
//        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));
//    }
//
//    @Test
//    void createTransaction_MissingAccount(){
//        TransactionRequest transactionRequest = new TransactionRequest(
//                "Debit",
//                TEST_TRANSACTION_AMOUNT,
//                TEST_CATEGORY_NAME,
//                TransactionType.DEBIT.toString(),
//                TEST_ACCOUNT_ID,
//                TEST_DATE);
//
//        Mockito.when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.empty());
//        Mockito.lenient().when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//
//        assertThrows(ResourceNotFoundException.class, () ->
//                transactionService.createTransaction(transactionRequest));
//
//        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));
//        Mockito.verify(transactionRepository, Mockito.times(0)).save(Mockito.any(Transaction.class));
//
//        assertEquals(TEST_STARTING_BALANCE, mockAccount.getBalance());
//
//    }
//
//    @Test
//    void createTransaction_MissingCategory(){
//        TransactionRequest transactionRequest = new TransactionRequest(
//                "Debit",
//                TEST_TRANSACTION_AMOUNT,
//                TEST_CATEGORY_NAME,
//                TransactionType.DEBIT.toString(),
//                TEST_ACCOUNT_ID,
//                TEST_DATE);
//
//        Mockito.when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(mockAccount));
//        Mockito.when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () ->
//                transactionService.createTransaction(transactionRequest));
//
//        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));
//        Mockito.verify(transactionRepository, Mockito.times(0)).save(Mockito.any(Transaction.class));
//
//        assertEquals(TEST_STARTING_BALANCE, mockAccount.getBalance());
//    }
//
//    @Test
//    void createTransaction_InvalidAmount(){
//        Mockito.when(accountRepository.findById(TEST_ACCOUNT_ID)).thenReturn(Optional.of(mockAccount));
//        Mockito.lenient().when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//
//        List<BigDecimal> invalidAmounts = Arrays.asList(
//                null,
//                BigDecimal.ZERO,
//                new BigDecimal("-10.00")
//        );
//
//        for (BigDecimal invalidAmount : invalidAmounts) {
//            TransactionRequest invalidRequest = new TransactionRequest(
//                    "Invalid Amount",
//                    invalidAmount,
//                    TEST_CATEGORY_NAME,
//                    TransactionType.DEBIT.toString(),
//                    TEST_ACCOUNT_ID,
//                    TEST_DATE);
//
//            assertThrows(IllegalArgumentException.class, () ->
//                    transactionService.createTransaction(invalidRequest)
//            );
//        }
//
//        assertEquals(TEST_STARTING_BALANCE, mockAccount.getBalance(), "Account balance should remain unchanged after validation failure.");
//        Mockito.verify(transactionRepository, Mockito.times(0)).save(Mockito.any(Transaction.class));
//
//        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));
//    }
//
//    @Test
//    void updateTransactionDebitToCredit(){
//        //Account with balance
//        mockAccount.setId(TEST_ACCOUNT_ID);
//        mockAccount.setBalance(new BigDecimal("1000.00"));
//
//        //Existing Transaction
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount);
//        existingTransaction.setCategory(mockCategory);
//        existingTransaction.setAmount(new BigDecimal("100.00"));
//        existingTransaction.setTransactionType(TransactionType.DEBIT);
//        existingTransaction.setDate(TEST_DATE);
//        existingTransaction.setName("Gas");
//
//        //New request
//        TransactionRequest transactionRequest = new TransactionRequest(
//                "Updated Paycheck",
//                new BigDecimal("200.00"),
//                TEST_CATEGORY_NAME,
//                "CREDIT",
//                TEST_ACCOUNT_ID,
//                TEST_DATE
//        );
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
//        Mockito.when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);
//        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(existingTransaction);
//
//        TransactionResponse response = transactionService.updateTransaction(TEST_TRANSACTION_ID, transactionRequest);
//
//        assertEquals(TransactionType.CREDIT, existingTransaction.getTransactionType());
//        assertEquals(new BigDecimal("200.00"), existingTransaction.getAmount(), "Amount should be updated");
//        assertEquals(new BigDecimal("1300.00"), mockAccount.getBalance());
//        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
//        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
//
//        assertNotNull(response);
//        assertEquals(transactionRequest.getTransactionType(), response.getTransactionType());
//    }
//
//    @Test
//    void updateTransactionCreditToDebit(){
//        //Account with balance
//        mockAccount.setId(TEST_ACCOUNT_ID);
//        mockAccount.setBalance(new BigDecimal("1000.00"));
//
//        //Existing Transaction
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount);
//        existingTransaction.setCategory(mockCategory);
//        existingTransaction.setAmount(new BigDecimal("200.00"));
//        existingTransaction.setTransactionType(TransactionType.CREDIT);
//        existingTransaction.setDate(TEST_DATE);
//        existingTransaction.setName("Paycheck");
//
//        //New request
//        TransactionRequest transactionRequest = new TransactionRequest(
//                "Car Repair",
//                new BigDecimal("100.00"),
//                TEST_CATEGORY_NAME,
//                "DEBIT",
//                TEST_ACCOUNT_ID,
//                TEST_DATE
//        );
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
//        Mockito.when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//        Mockito.when(accountRepository.save(Mockito.any(Account.class))).thenReturn(mockAccount);
//        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(existingTransaction);
//
//        TransactionResponse response = transactionService.updateTransaction(TEST_TRANSACTION_ID, transactionRequest);
//
//        assertEquals(TransactionType.DEBIT, existingTransaction.getTransactionType());
//        assertEquals(new BigDecimal("100.00"), existingTransaction.getAmount(), "Amount should be updated");
//        assertEquals(new BigDecimal("700.00"), mockAccount.getBalance());
//        Mockito.verify(accountRepository, Mockito.times(1)).save(Mockito.any(Account.class));
//        Mockito.verify(transactionRepository, Mockito.times(1)).save(Mockito.any(Transaction.class));
//
//        assertNotNull(response);
//        assertEquals(transactionRequest.getTransactionType(), response.getTransactionType());
//    }
//
//
//    @Test
//    void updateTransaction_NotFound(){
//        //Account with balance
//        mockAccount.setId(TEST_ACCOUNT_ID);
//        mockAccount.setBalance(new BigDecimal("1000.00"));
//
//        //Existing Transaction
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount);
//        existingTransaction.setCategory(mockCategory);
//        existingTransaction.setAmount(new BigDecimal("200.00"));
//        existingTransaction.setTransactionType(TransactionType.CREDIT);
//        existingTransaction.setDate(TEST_DATE);
//        existingTransaction.setName("Paycheck");
//
//        //New request
//        TransactionRequest transactionRequest = new TransactionRequest(
//                "Car Repair",
//                new BigDecimal("100.00"),
//                TEST_CATEGORY_NAME,
//                "DEBIT",
//                TEST_ACCOUNT_ID,
//                TEST_DATE
//        );
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.empty());
//        Mockito.lenient().when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//
//        assertThrows(ResourceNotFoundException.class, () ->
//                transactionService.updateTransaction(TEST_TRANSACTION_ID, transactionRequest));
//
//        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));
//        Mockito.verify(transactionRepository, Mockito.times(0)).save(Mockito.any(Transaction.class));
//    }
//
//    @Test
//    void updateTransaction_MissingCategory(){
//        mockAccount.setBalance(new BigDecimal("1000.00"));
//
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount);
//        existingTransaction.setCategory(mockCategory);
//        existingTransaction.setAmount(new BigDecimal("100.00"));
//        existingTransaction.setTransactionType(TransactionType.DEBIT);
//        existingTransaction.setName("Old Rent Debit");
//        existingTransaction.setDate(TEST_DATE);
//
//        final String NON_EXISTENT_CATEGORY = "NonExistent";
//        TransactionRequest newRequest = new TransactionRequest(
//                "Updated Item",
//                new BigDecimal("200.00"),
//                NON_EXISTENT_CATEGORY,
//                TransactionType.DEBIT.toString(),
//                TEST_ACCOUNT_ID,
//                TEST_DATE
//        );
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
//        Mockito.when(categoryRepository.findByName(NON_EXISTENT_CATEGORY)).thenReturn(Optional.empty());
//
//        assertThrows(ResourceNotFoundException.class, () ->
//                transactionService.updateTransaction(TEST_TRANSACTION_ID, newRequest)
//        );
//
//        assertEquals(new BigDecimal("1000.00"), mockAccount.getBalance());
//        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
//        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
//
//    }
//
//    @Test
//    void updateTransaction_AccountMismatch(){
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount); // Account ID is TEST_ACCOUNT_ID (1L)
//        existingTransaction.setAmount(new BigDecimal("100.00"));
//        existingTransaction.setTransactionType(TransactionType.DEBIT);
//
//        TransactionRequest mismatchRequest = new TransactionRequest(
//                "Update attempt",
//                new BigDecimal("50.00"),
//                TEST_CATEGORY_NAME,
//                TransactionType.CREDIT.toString(),
//                99L, // Request Account ID is TEST_OTHER_ACCOUNT_ID (99L)
//                TEST_DATE
//        );
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
//
//        Mockito.lenient().when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//
//        assertThrows(IllegalArgumentException.class, () ->
//                        transactionService.updateTransaction(TEST_TRANSACTION_ID, mismatchRequest),
//                "Should throw IllegalArgumentException when the transaction's account ID does not match the request's account ID."
//        );
//
//        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
//        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
//    }
//
//    @Test
//    void updateTransaction_InvalidAmount(){
//        mockAccount.setBalance(new BigDecimal("1000.00"));
//        final BigDecimal startingBalance = mockAccount.getBalance();
//
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount);
//        existingTransaction.setAmount(new BigDecimal("100.00"));
//        existingTransaction.setTransactionType(TransactionType.DEBIT);
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
//        Mockito.lenient().when(categoryRepository.findByName(TEST_CATEGORY_NAME)).thenReturn(Optional.of(mockCategory));
//
//        List<BigDecimal> invalidAmounts = Arrays.asList(
//                null,
//                BigDecimal.ZERO,
//                new BigDecimal("-5.00")
//        );
//
//        for (BigDecimal invalidAmount : invalidAmounts) {
//            TransactionRequest invalidRequest = new TransactionRequest(
//                    "Invalid Amount Update",
//                    invalidAmount,
//                    TEST_CATEGORY_NAME,
//                    TransactionType.CREDIT.toString(),
//                    TEST_ACCOUNT_ID,
//                    TEST_DATE);
//
//            // ACT & ASSERT
//            assertThrows(IllegalArgumentException.class, () ->
//                            transactionService.updateTransaction(TEST_TRANSACTION_ID, invalidRequest),
//                    "Should throw IllegalArgumentException for null, zero, or negative amount: " + invalidAmount
//            );
//        }
//
//        assertEquals(startingBalance, mockAccount.getBalance(), "Account balance should remain unchanged after validation failure.");
//        Mockito.verify(accountRepository, Mockito.times(0)).save(Mockito.any(Account.class));
//        Mockito.verify(transactionRepository, Mockito.times(0)).save(Mockito.any(Transaction.class));
//    }
//
//    @Test
//    void deleteCreditTransaction_Success() {
//        //Account with balance
//        mockAccount.setId(TEST_ACCOUNT_ID);
//        mockAccount.setBalance(new BigDecimal("1000.00"));
//
//        //Existing Transaction
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount);
//        existingTransaction.setCategory(mockCategory);
//        existingTransaction.setAmount(new BigDecimal("200.00"));
//        existingTransaction.setTransactionType(TransactionType.CREDIT);
//        existingTransaction.setDate(TEST_DATE);
//        existingTransaction.setName("Paycheck");
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
//
//        transactionService.deleteTransaction(TEST_TRANSACTION_ID);
//
//        assertEquals(new BigDecimal("800.00"), mockAccount.getBalance());
//
//        Mockito.verify(accountRepository, Mockito.times(1)).save(mockAccount);
//        Mockito.verify(transactionRepository, Mockito.times(1)).delete(existingTransaction);
//    }
//
//    @Test
//    void deleteDebitTransaction_Success() {
//        //Account with balance
//        mockAccount.setId(TEST_ACCOUNT_ID);
//        mockAccount.setBalance(new BigDecimal("1000.00"));
//
//        //Existing Transaction
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount);
//        existingTransaction.setCategory(mockCategory);
//        existingTransaction.setAmount(new BigDecimal("200.00"));
//        existingTransaction.setTransactionType(TransactionType.DEBIT);
//        existingTransaction.setDate(TEST_DATE);
//        existingTransaction.setName("Lost the money");
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
//
//        transactionService.deleteTransaction(TEST_TRANSACTION_ID);
//
//        assertEquals(new BigDecimal("1200.00"), mockAccount.getBalance());
//
//        Mockito.verify(accountRepository, Mockito.times(1)).save(mockAccount);
//        Mockito.verify(transactionRepository, Mockito.times(1)).delete(existingTransaction);
//    }
//
//    @Test
//    void deleteTransaction_NotFound() {
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.empty());
//        assertThrows(ResourceNotFoundException.class, () ->
//                transactionService.deleteTransaction(TEST_TRANSACTION_ID));
//    }
//
//    @Test
//    void getTransactionById_Found(){
//        Transaction existingTransaction = new Transaction();
//        existingTransaction.setId(TEST_TRANSACTION_ID);
//        existingTransaction.setAccount(mockAccount);
//        existingTransaction.setCategory(mockCategory);
//        existingTransaction.setAmount(new BigDecimal("200.00"));
//        existingTransaction.setTransactionType(TransactionType.DEBIT);
//        existingTransaction.setDate(TEST_DATE);
//        existingTransaction.setName("Lost the money");
//
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.of(existingTransaction));
//
//        Optional<TransactionResponse> response = transactionService.getById(TEST_TRANSACTION_ID);
//
//        assertTrue(response.isPresent());
//        assertEquals(TEST_TRANSACTION_ID, response.get().getId());
//        assertEquals(new BigDecimal("200.00"), response.get().getAmount());
//        assertEquals(TransactionType.DEBIT.toString(), response.get().getTransactionType());
//        assertEquals("Lost the money", response.get().getName());
//
//        Mockito.verify(transactionRepository, Mockito.times(1)).findById(TEST_TRANSACTION_ID);
//        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any());
//
//    }
//
//    @Test
//    void getTransactionById_NotFound(){
//        Mockito.when(transactionRepository.findById(TEST_TRANSACTION_ID)).thenReturn(Optional.empty());
//
//        Optional<TransactionResponse> response = transactionService.getById(TEST_TRANSACTION_ID);
//
//        assertFalse(response.isPresent());
//
//        Mockito.verify(transactionRepository, Mockito.times(1)).findById(TEST_TRANSACTION_ID);
//        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any());
//    }
//
//    @Test
//    void getTransactionsByAccountId_WithResults(){
//
//    }
//
//
//
//
//
//}
