package com.bank.service;

import com.bank.dto.TransactionResponse;
import com.bank.dto.TransactionStatsResponse;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.repository.TransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final AlertService alertService;
    private final ReportService reportService;

    public TransactionService(AccountService accountService,
                               TransactionRepository transactionRepository,
                               AlertService alertService,
                               ReportService reportService) {
        this.accountService = accountService;
        this.transactionRepository = transactionRepository;
        this.alertService = alertService;
        this.reportService = reportService;
    }

    @Transactional
    public TransactionResponse deposit(String accountNumber, BigDecimal amount) {
        validatePositive(amount);
        Account account = accountService.findOrThrow(accountNumber);
        account.credit(amount);
        Transaction tx = new Transaction(TransactionType.DEPOSIT, account, amount,
                null, account.getBalance(), "Deposit");
        tx = transactionRepository.save(tx);
        reportService.log("DEPOSIT", accountNumber, amount.doubleValue(), null);
        alertService.checkLowBalance(account);
        return new TransactionResponse(tx);
    }

    @Transactional
    public TransactionResponse withdraw(String accountNumber, BigDecimal amount) {
        validatePositive(amount);
        Account account = accountService.findOrThrow(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(accountNumber, account.getBalance(), amount);
        }
        account.debit(amount);
        Transaction tx = new Transaction(TransactionType.WITHDRAWAL, account, amount,
                null, account.getBalance(), "Withdrawal");
        tx = transactionRepository.save(tx);
        reportService.log("WITHDRAWAL", accountNumber, amount.doubleValue(), null);
        alertService.checkLowBalance(account);
        return new TransactionResponse(tx);
    }

    @Transactional
    public List<TransactionResponse> transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        validatePositive(amount);
        Account sender = accountService.findOrThrow(fromAccountNumber);
        Account receiver = accountService.findOrThrow(toAccountNumber);

        if (sender.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(fromAccountNumber, sender.getBalance(), amount);
        }

        sender.debit(amount);
        receiver.credit(amount);

        Transaction debit = new Transaction(TransactionType.TRANSFER_DEBIT, sender, amount,
                toAccountNumber, sender.getBalance(), "Transfer to " + toAccountNumber);
        Transaction credit = new Transaction(TransactionType.TRANSFER_CREDIT, receiver, amount,
                fromAccountNumber, receiver.getBalance(), "Transfer from " + fromAccountNumber);

        debit = transactionRepository.save(debit);
        transactionRepository.save(credit);

        reportService.log("TRANSFER", fromAccountNumber, amount.doubleValue(), toAccountNumber);
        alertService.checkLowBalance(sender);
        alertService.checkLowBalance(receiver);

        return List.of(new TransactionResponse(debit));
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getAccountTransactions(String accountNumber) {
        accountService.findOrThrow(accountNumber);
        return transactionRepository.findByAccount_AccountNumberOrderByTimestampDesc(accountNumber)
                .stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<TransactionResponse> getAccountTransactionsPaged(String accountNumber, Pageable pageable) {
        accountService.findOrThrow(accountNumber);
        return transactionRepository.findByAccount_AccountNumberOrderByTimestampDesc(accountNumber, pageable)
                .map(TransactionResponse::new);
    }

    @Transactional(readOnly = true)
    public TransactionStatsResponse getStats() {
        long total = transactionRepository.count();
        long totalAccounts = accountService.count();
        BigDecimal totalBalance = accountService.totalBalance();

        Map<String, Long> countByType = new LinkedHashMap<>();
        Map<String, BigDecimal> volumeByType = new LinkedHashMap<>();

        for (TransactionType type : EnumSet.allOf(TransactionType.class)) {
            countByType.put(type.name(), transactionRepository.countByType(type));
            volumeByType.put(type.name(), transactionRepository.sumAmountByType(type));
        }

        return new TransactionStatsResponse(total, countByType, volumeByType, totalAccounts, totalBalance);
    }

    private void validatePositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
    }
}
