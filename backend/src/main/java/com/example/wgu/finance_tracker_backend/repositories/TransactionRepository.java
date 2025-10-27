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

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId AND t.account.user.userName = :username")
    List<Transaction> findByAccountIdAndUserName(@Param("accountId") Long accountId, @Param("username") String username);



    @Query("SELECT t FROM Transaction t WHERE t.account.user.userName = :username AND MONTH(t.date) = :month AND YEAR(t.date) = :year")
    List<Transaction> findByUserAndMonthAndYear(@Param("username") String username, @Param("month") int month, @Param("year") int year);
}
