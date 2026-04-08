package com.bank.service;

import com.bank.dto.SimulationRequest;
import com.bank.dto.SimulationResult;
import com.bank.exception.InsufficientBalanceException;
import com.bank.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class SimulationService {

    private static final Logger log = LoggerFactory.getLogger(SimulationService.class);

    private static final String[] NAMES = {
            "Alice", "Bob", "Carol", "David", "Eve", "Frank", "Grace", "Hank",
            "Ivy", "Jack", "Karen", "Leo", "Mia", "Noah", "Olivia", "Paul",
            "Quinn", "Rose", "Sam", "Tina", "Uma", "Victor", "Wendy", "Xander"
    };

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final Random random = new Random();

    public SimulationService(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Transactional
    public SimulationResult run(SimulationRequest request) {
        List<String> eventLog = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();

        // Create accounts
        for (int i = 0; i < request.getNumberOfAccounts(); i++) {
            String name = NAMES[i % NAMES.length] + (i >= NAMES.length ? "_" + (i / NAMES.length) : "");
            String email = name.toLowerCase() + "@sim.bank";
            BigDecimal startBalance = randomAmount(1000, 10000);
            Account account = accountService.createAccountInternal(name, email, startBalance);
            accounts.add(account);
            eventLog.add(String.format("CREATED account %s for %s with balance $%.2f",
                    account.getAccountNumber(), name, startBalance));
        }

        int succeeded = 0;
        int failed = 0;

        // Run random transactions
        for (int i = 0; i < request.getNumberOfTransactions(); i++) {
            int op = random.nextInt(3); // 0=deposit, 1=withdraw, 2=transfer
            Account account = accounts.get(random.nextInt(accounts.size()));
            BigDecimal amount = randomAmount(50, 2000);

            try {
                switch (op) {
                    case 0 -> {
                        transactionService.deposit(account.getAccountNumber(), amount);
                        eventLog.add(String.format("DEPOSIT $%.2f to %s", amount, account.getAccountNumber()));
                        succeeded++;
                    }
                    case 1 -> {
                        transactionService.withdraw(account.getAccountNumber(), amount);
                        eventLog.add(String.format("WITHDRAW $%.2f from %s", amount, account.getAccountNumber()));
                        succeeded++;
                    }
                    case 2 -> {
                        Account other = accounts.get(random.nextInt(accounts.size()));
                        if (!other.getAccountNumber().equals(account.getAccountNumber())) {
                            transactionService.transfer(account.getAccountNumber(), other.getAccountNumber(), amount);
                            eventLog.add(String.format("TRANSFER $%.2f from %s to %s",
                                    amount, account.getAccountNumber(), other.getAccountNumber()));
                            succeeded++;
                        }
                    }
                }
            } catch (InsufficientBalanceException ex) {
                eventLog.add(String.format("FAILED (insufficient balance) $%.2f on %s", amount, account.getAccountNumber()));
                failed++;
            } catch (Exception ex) {
                eventLog.add(String.format("FAILED (%s) on %s", ex.getMessage(), account.getAccountNumber()));
                failed++;
            }
        }

        log.info("Simulation complete: {} accounts, {} succeeded, {} failed",
                accounts.size(), succeeded, failed);

        return new SimulationResult(accounts.size(), request.getNumberOfTransactions(), succeeded, failed, eventLog);
    }

    private BigDecimal randomAmount(int min, int max) {
        double value = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }
}
