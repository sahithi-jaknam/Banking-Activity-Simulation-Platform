package com.bank.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "target_account_number")
    private String targetAccountNumber;

    @Column(name = "balance_after", precision = 19, scale = 2)
    private BigDecimal balanceAfter;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column
    private String note;

    protected Transaction() {}

    public Transaction(TransactionType type, Account account, BigDecimal amount,
                       String targetAccountNumber, BigDecimal balanceAfter, String note) {
        this.type = type;
        this.account = account;
        this.amount = amount;
        this.targetAccountNumber = targetAccountNumber;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
        this.note = note;
    }

    public Long getId() { return id; }

    public TransactionType getType() { return type; }

    public Account getAccount() { return account; }

    public BigDecimal getAmount() { return amount; }

    public String getTargetAccountNumber() { return targetAccountNumber; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public String getNote() { return note; }
}
