package com.example.wgu.finance_tracker_backend.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Account implements Transactionable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String accountName;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactions;

    public Account() {
    }

    public Account(Long id, String accountName, BigDecimal balance, List<Transaction> transactions) {
        this.id = id;
        this.accountName = accountName;
        this.balance = balance;
        this.transactions = transactions;
    }

    public Account(Long id, Long userId, String accountName, BigDecimal balance, AccountType accountType, List<Transaction> transactions) {
        this.id = id;
        this.userId = userId;
        this.accountName = accountName;
        this.balance = balance;
        this.accountType = accountType;
        this.transactions = transactions;
    }

    @Override
    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Override
    public void debit(BigDecimal amount) {
        balance = balance.subtract(amount);
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

}
