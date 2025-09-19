package com.example.wgu.finance_tracker_backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class SavingAccount extends Account{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal interestRate;

    public SavingAccount(Long id, String accountName, BigDecimal balance, List<Transaction> transactions) {
        super(id, accountName, balance, transactions);
    }

    public SavingAccount() {
    }

    public SavingAccount(Long id, BigDecimal interestRate) {
        this.id = id;
        this.interestRate = interestRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
}
