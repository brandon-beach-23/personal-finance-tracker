package com.example.wgu.finance_tracker_backend.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private double amount;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Transaction() {
    }

    public Transaction(Long id, String description, double amount, LocalDateTime date, User user, Category category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.user = user;
        this.category = category;
    }

    public Transaction(Long id, String description, double amount, LocalDateTime date, TransactionType transactionType, User user, Category category) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.transactionType = transactionType;
        this.user = user;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
