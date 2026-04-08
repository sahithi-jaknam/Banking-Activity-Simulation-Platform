package com.bank.controller;

import com.bank.dto.TransactionResponse;
import com.bank.dto.TransactionStatsResponse;
import com.bank.dto.TransferRequest;
import com.bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<List<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transactionService.transfer(
                request.getFromAccountNumber(),
                request.getToAccountNumber(),
                request.getAmount()));
    }

    @GetMapping("/stats")
    public ResponseEntity<TransactionStatsResponse> getStats() {
        return ResponseEntity.ok(transactionService.getStats());
    }
}
