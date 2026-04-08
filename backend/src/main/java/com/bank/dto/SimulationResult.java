package com.bank.dto;

import java.util.List;

public class SimulationResult {

    private int accountsCreated;
    private int transactionsAttempted;
    private int transactionsSucceeded;
    private int transactionsFailed;
    private List<String> log;

    public SimulationResult(int accountsCreated, int transactionsAttempted,
                            int transactionsSucceeded, int transactionsFailed, List<String> log) {
        this.accountsCreated = accountsCreated;
        this.transactionsAttempted = transactionsAttempted;
        this.transactionsSucceeded = transactionsSucceeded;
        this.transactionsFailed = transactionsFailed;
        this.log = log;
    }

    public int getAccountsCreated() { return accountsCreated; }

    public int getTransactionsAttempted() { return transactionsAttempted; }

    public int getTransactionsSucceeded() { return transactionsSucceeded; }

    public int getTransactionsFailed() { return transactionsFailed; }

    public List<String> getLog() { return log; }
}
