package com.bank.service;

import com.bank.dto.TransactionResponse;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AlertService alertService;

    @Mock
    private ReportService reportService;

    @InjectMocks
    private TransactionService transactionService;

    private Account alice;
    private Account bob;

    @BeforeEach
    void setUp() {
        alice = new Account("Alice", "alice@test.com", new BigDecimal("5000.00"));
        alice.setAccountNumber("10000001");

        bob = new Account("Bob", "bob@test.com", new BigDecimal("1000.00"));
        bob.setAccountNumber("10000002");
    }

    private Transaction stubSave(TransactionType type, Account account, BigDecimal amount) {
        Transaction tx = new Transaction(type, account, amount, null, account.getBalance(), "test");
        when(transactionRepository.save(any(Transaction.class))).thenReturn(tx);
        return tx;
    }

    @Test
    void deposit_creditsAccountAndReturnsResponse() {
        when(accountService.findOrThrow("10000001")).thenReturn(alice);
        stubSave(TransactionType.DEPOSIT, alice, new BigDecimal("500.00"));

        TransactionResponse response = transactionService.deposit("10000001", new BigDecimal("500.00"));

        assertThat(alice.getBalance()).isEqualByComparingTo("5500.00");
        assertThat(response).isNotNull();
        verify(reportService).log(eq("DEPOSIT"), eq("10000001"), eq(500.0), isNull());
        verify(alertService).checkLowBalance(alice);
    }

    @Test
    void deposit_withZeroAmount_throwsInvalidAmountException() {
        assertThatThrownBy(() -> transactionService.deposit("10000001", BigDecimal.ZERO))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void withdraw_debitsAccountAndReturnsResponse() {
        when(accountService.findOrThrow("10000001")).thenReturn(alice);
        stubSave(TransactionType.WITHDRAWAL, alice, new BigDecimal("200.00"));

        TransactionResponse response = transactionService.withdraw("10000001", new BigDecimal("200.00"));

        assertThat(alice.getBalance()).isEqualByComparingTo("4800.00");
        assertThat(response).isNotNull();
    }

    @Test
    void withdraw_exceedingBalance_throwsInsufficientBalanceException() {
        when(accountService.findOrThrow("10000001")).thenReturn(alice);

        assertThatThrownBy(() -> transactionService.withdraw("10000001", new BigDecimal("9999.00")))
                .isInstanceOf(InsufficientBalanceException.class);
    }

    @Test
    void transfer_movesMoneyBetweenAccounts() {
        when(accountService.findOrThrow("10000001")).thenReturn(alice);
        when(accountService.findOrThrow("10000002")).thenReturn(bob);
        Transaction debit = new Transaction(TransactionType.TRANSFER_DEBIT, alice, new BigDecimal("300.00"),
                "10000002", alice.getBalance(), "test");
        when(transactionRepository.save(any(Transaction.class))).thenReturn(debit);

        List<TransactionResponse> responses = transactionService.transfer("10000001", "10000002", new BigDecimal("300.00"));

        assertThat(alice.getBalance()).isEqualByComparingTo("4700.00");
        assertThat(bob.getBalance()).isEqualByComparingTo("1300.00");
        assertThat(responses).isNotEmpty();
    }

    @Test
    void transfer_insufficientFunds_throwsException() {
        when(accountService.findOrThrow("10000001")).thenReturn(alice);
        when(accountService.findOrThrow("10000002")).thenReturn(bob);

        assertThatThrownBy(() -> transactionService.transfer("10000001", "10000002", new BigDecimal("9999.00")))
                .isInstanceOf(InsufficientBalanceException.class);
    }
}
