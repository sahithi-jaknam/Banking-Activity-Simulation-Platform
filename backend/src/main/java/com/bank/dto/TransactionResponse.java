package com.bank.dto;

import com.bank.model.Transaction;
import com.bank.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponse {

    private Long id;
    private TransactionType type;
    private String accountNumber;
    private BigDecimal amount;
    private String targetAccountNumber;
    private BigDecimal balanceAfter;
    private LocalDateTime timestamp;
    private String note;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.accountNumber = transaction.getAccount().getAccountNumber();
        this.amount = transaction.getAmount();
        this.targetAccountNumber = transaction.getTargetAccountNumber();
        this.balanceAfter = transaction.getBalanceAfter();
        this.timestamp = transaction.getTimestamp();
        this.note = transaction.getNote();
    }

    public Long getId() { return id; }

    public TransactionType getType() { return type; }

    public String getAccountNumber() { return accountNumber; }

    public BigDecimal getAmount() { return amount; }

    public String getTargetAccountNumber() { return targetAccountNumber; }

    public BigDecimal getBalanceAfter() { return balanceAfter; }

    public LocalDateTime getTimestamp() { return timestamp; }

    public String getNote() { return note; }
}
