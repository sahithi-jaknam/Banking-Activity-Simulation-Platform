package com.bank.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_seq", initialValue = 10000000, allocationSize = 1)
    private Long id;

    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @NotBlank
    @Column(name = "holder_name", nullable = false)
    private String holderName;

    @Email
    @NotBlank
    @Column(nullable = false)
    private String email;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    protected Account() {}

    public Account(String holderName, String email, BigDecimal balance) {
        this.holderName = holderName;
        this.email = email;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    private void generateAccountNumber() {
        if (this.accountNumber == null) {
            this.accountNumber = String.valueOf(id);
        }
    }

    public void credit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void debit(BigDecimal amount) {
        this.balance = this.balance.subtract(amount);
    }

    // Getters

    public Long getId() { return id; }

    public String getAccountNumber() { return accountNumber; }

    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getHolderName() { return holderName; }

    public String getEmail() { return email; }

    public BigDecimal getBalance() { return balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Transaction> getTransactions() { return transactions; }
}
