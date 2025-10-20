package com.example.wgu.finance_tracker_backend.repositories;

import com.example.wgu.finance_tracker_backend.models.Category;
import com.example.wgu.finance_tracker_backend.models.Transaction;
import com.example.wgu.finance_tracker_backend.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTransactionId(Long transactionId);
    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findByCategoryAndAccountId(Category category, Long accountId);
    List<Transaction> findByDateAndAccountId(LocalDateTime date, Long accountId);
    List<Transaction> findByAmountAndAccountId(BigDecimal amount, Long accountId);
    List<Transaction> findByNameAndAccountId(String name, Long accountId);
    List<Transaction> findByTransactionTypeAndAccountId(TransactionType transactionType, Long accountId);
    boolean existsByTransactionIdAndAccountId(Long transactionId, Long accountId);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.account.id = :accountId AND t.transactionType = :transactionType")
    BigDecimal getSumAmountByAccountIdAndTransactionType(@Param("accountId") Long accountId,@Param("transactionType") TransactionType transactionType);
}
