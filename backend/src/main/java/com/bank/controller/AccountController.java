package com.bank.controller;

import com.bank.dto.AccountResponse;
import com.bank.dto.AmountRequest;
import com.bank.dto.CreateAccountRequest;
import com.bank.dto.TransactionResponse;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccount(accountNumber));
    }

    @PostMapping("/{accountNumber}/deposit")
    public ResponseEntity<TransactionResponse> deposit(@PathVariable String accountNumber,
                                                       @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(transactionService.deposit(accountNumber, request.getAmount()));
    }

    @PostMapping("/{accountNumber}/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@PathVariable String accountNumber,
                                                        @Valid @RequestBody AmountRequest request) {
        return ResponseEntity.ok(transactionService.withdraw(accountNumber, request.getAmount()));
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getAccountTransactions(accountNumber));
    }
}
