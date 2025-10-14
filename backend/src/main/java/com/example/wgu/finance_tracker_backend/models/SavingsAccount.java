package com.example.wgu.finance_tracker_backend.models;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
public class SavingsAccount extends Account{

    @OneToOne(mappedBy = "savingsAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private SavingsGoal savingsGoal;

    public SavingsAccount() {
    }

    public SavingsGoal getSavingsGoal() {
        return savingsGoal;
    }
    public void setSavingsGoal(SavingsGoal savingsGoal) {
        this.savingsGoal = savingsGoal;
    }
}
