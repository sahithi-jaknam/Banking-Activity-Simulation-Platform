package com.bank.dto;

import com.bank.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {

    private String accountNumber;
    private String holderName;
    private String email;
    private BigDecimal balance;
    private LocalDateTime createdAt;

    public AccountResponse(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.holderName = account.getHolderName();
        this.email = account.getEmail();
        this.balance = account.getBalance();
        this.createdAt = account.getCreatedAt();
    }

    public String getAccountNumber() { return accountNumber; }

    public String getHolderName() { return holderName; }

    public String getEmail() { return email; }

    public BigDecimal getBalance() { return balance; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
