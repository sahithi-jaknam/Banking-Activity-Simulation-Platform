package com.bank.service;

import com.bank.dto.AccountResponse;
import com.bank.dto.CreateAccountRequest;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.InvalidAmountException;
import com.bank.model.Account;
import com.bank.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountResponse createAccount(CreateAccountRequest request) {
        if (request.getOpeningBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Opening balance cannot be negative");
        }
        Account account = new Account(request.getName(), request.getEmail(), request.getOpeningBalance());
        account = accountRepository.save(account);
        // Use the generated ID as account number
        account.setAccountNumber(String.valueOf(account.getId() + 10000000L - 1));
        account = accountRepository.save(account);
        return new AccountResponse(account);
    }

    @Transactional(readOnly = true)
    public AccountResponse getAccount(String accountNumber) {
        Account account = findOrThrow(accountNumber);
        return new AccountResponse(account);
    }

    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Account findOrThrow(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(accountNumber));
    }

    @Transactional(readOnly = true)
    public long count() {
        return accountRepository.count();
    }

    @Transactional(readOnly = true)
    public BigDecimal totalBalance() {
        return accountRepository.sumAllBalances();
    }

    // Used internally by simulation — creates an account without DTO
    @Transactional
    public Account createAccountInternal(String name, String email, BigDecimal openingBalance) {
        Account account = new Account(name, email, openingBalance);
        account = accountRepository.save(account);
        account.setAccountNumber(String.valueOf(account.getId() + 10000000L - 1));
        return accountRepository.save(account);
    }
}
