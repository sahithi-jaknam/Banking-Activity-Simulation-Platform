package com.bank.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class SimulationRequest {

    @NotNull
    @Min(value = 2, message = "Must have at least 2 accounts")
    @Max(value = 50, message = "Maximum 50 accounts per simulation")
    private int numberOfAccounts;

    @NotNull
    @Min(value = 1, message = "Must run at least 1 transaction")
    @Max(value = 500, message = "Maximum 500 transactions per simulation")
    private int numberOfTransactions;

    public int getNumberOfAccounts() { return numberOfAccounts; }

    public void setNumberOfAccounts(int numberOfAccounts) { this.numberOfAccounts = numberOfAccounts; }

    public int getNumberOfTransactions() { return numberOfTransactions; }

    public void setNumberOfTransactions(int numberOfTransactions) { this.numberOfTransactions = numberOfTransactions; }
}
