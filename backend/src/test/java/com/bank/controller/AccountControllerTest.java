package com.bank.controller;

import com.bank.dto.AccountResponse;
import com.bank.dto.TransactionResponse;
import com.bank.exception.AccountNotFoundException;
import com.bank.exception.GlobalExceptionHandler;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@Import(GlobalExceptionHandler.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    private AccountResponse sampleAccountResponse() {
        Account account = new Account("Alice", "alice@test.com", new BigDecimal("5000.00"));
        account.setAccountNumber("10000001");
        return new AccountResponse(account);
    }

    @Test
    void createAccount_returnsCreated() throws Exception {
        when(accountService.createAccount(any())).thenReturn(sampleAccountResponse());

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("name", "Alice", "email", "alice@test.com", "openingBalance", "5000.00"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.holderName").value("Alice"))
                .andExpect(jsonPath("$.balance").value(5000.00));
    }

    @Test
    void createAccount_missingName_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("email", "alice@test.com", "openingBalance", "5000.00"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAccount_withValidNumber_returnsAccount() throws Exception {
        when(accountService.getAccount("10000001")).thenReturn(sampleAccountResponse());

        mockMvc.perform(get("/api/accounts/10000001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("10000001"));
    }

    @Test
    void getAccount_withUnknownNumber_returnsNotFound() throws Exception {
        when(accountService.getAccount("99999999")).thenThrow(new AccountNotFoundException("99999999"));

        mockMvc.perform(get("/api/accounts/99999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Account Not Found"));
    }

    @Test
    void getAllAccounts_returnsList() throws Exception {
        when(accountService.getAllAccounts()).thenReturn(List.of(sampleAccountResponse()));

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void deposit_withValidRequest_returnsOk() throws Exception {
        Account account = new Account("Alice", "alice@test.com", new BigDecimal("5500.00"));
        account.setAccountNumber("10000001");
        Transaction tx = new Transaction(TransactionType.DEPOSIT, account,
                new BigDecimal("500.00"), null, new BigDecimal("5500.00"), "Deposit");
        when(transactionService.deposit(eq("10000001"), any())).thenReturn(new TransactionResponse(tx));

        mockMvc.perform(post("/api/accounts/10000001/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("amount", "500.00"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("DEPOSIT"));
    }
}
