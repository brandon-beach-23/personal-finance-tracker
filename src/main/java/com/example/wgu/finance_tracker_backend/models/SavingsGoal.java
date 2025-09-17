package com.example.wgu.finance_tracker_backend.models;


import jakarta.persistence.*;
import org.hibernate.mapping.Join;

@Entity
public class SavingsGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String goalName;
    private double targetAmount;
    private double currentAmount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SavingsGoal(int id, String goalName, double targetAmount, double currentAmount, User user) {
        this.id = id;
        this.goalName = goalName;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.user = user;
    }
}
