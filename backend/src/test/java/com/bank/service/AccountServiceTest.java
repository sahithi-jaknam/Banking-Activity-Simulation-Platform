package com.bank.service;

import com.bank.dto.AccountResponse;
import com.bank.dto.CreateAccountRequest;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account sampleAccount;

    @BeforeEach
    void setUp() {
        sampleAccount = new Account("Alice", "alice@test.com", new BigDecimal("5000.00"));
        sampleAccount.setAccountNumber("10000001");
    }

    @Test
    void createAccount_withPositiveBalance_returnsResponse() {
        when(accountRepository.save(any(Account.class))).thenReturn(sampleAccount);

        CreateAccountRequest req = new CreateAccountRequest();
        req.setName("Alice");
        req.setEmail("alice@test.com");
        req.setOpeningBalance(new BigDecimal("5000.00"));

        AccountResponse response = accountService.createAccount(req);

        assertThat(response).isNotNull();
        assertThat(response.getHolderName()).isEqualTo("Alice");
        assertThat(response.getBalance()).isEqualByComparingTo("5000.00");
    }

    @Test
    void createAccount_withNegativeBalance_throwsInvalidAmountException() {
        CreateAccountRequest req = new CreateAccountRequest();
        req.setName("Bob");
        req.setEmail("bob@test.com");
        req.setOpeningBalance(new BigDecimal("-100.00"));

        assertThatThrownBy(() -> accountService.createAccount(req))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessageContaining("negative");
    }

    @Test
    void getAccount_withValidNumber_returnsAccount() {
        when(accountRepository.findByAccountNumber("10000001")).thenReturn(Optional.of(sampleAccount));

        AccountResponse response = accountService.getAccount("10000001");

        assertThat(response.getAccountNumber()).isEqualTo("10000001");
        assertThat(response.getHolderName()).isEqualTo("Alice");
    }

    @Test
    void getAccount_withUnknownNumber_throwsAccountNotFoundException() {
        when(accountRepository.findByAccountNumber("99999999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount("99999999"))
                .isInstanceOf(AccountNotFoundException.class)
                .hasMessageContaining("99999999");
    }

    @Test
    void getAllAccounts_returnsAllAccounts() {
        Account second = new Account("Bob", "bob@test.com", new BigDecimal("2000.00"));
        second.setAccountNumber("10000002");
        when(accountRepository.findAll()).thenReturn(List.of(sampleAccount, second));

        List<AccountResponse> all = accountService.getAllAccounts();

        assertThat(all).hasSize(2);
    }

    @Test
    void creditAndDebit_updateBalanceCorrectly() {
        sampleAccount.credit(new BigDecimal("500.00"));
        assertThat(sampleAccount.getBalance()).isEqualByComparingTo("5500.00");

        sampleAccount.debit(new BigDecimal("200.00"));
        assertThat(sampleAccount.getBalance()).isEqualByComparingTo("5300.00");
    }
}
