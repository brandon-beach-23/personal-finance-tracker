package com.example.wgu.finance_tracker_backend.models;


import jakarta.persistence.*;
import org.hibernate.mapping.Join;

@Entity
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String goalName;
    private double targetAmount;

    private Long accountId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public SavingsGoal() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SavingsGoal(Long id, String goalName, double targetAmount, User user) {
        this.id = id;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.user = user;
    }
}
