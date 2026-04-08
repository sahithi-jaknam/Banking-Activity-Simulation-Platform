package com.bank.dto;

import java.math.BigDecimal;
import java.util.Map;

public class TransactionStatsResponse {

    private long totalTransactions;
    private Map<String, Long> countByType;
    private Map<String, BigDecimal> volumeByType;
    private long totalAccounts;
    private BigDecimal totalBalanceAcrossAllAccounts;

    public TransactionStatsResponse(long totalTransactions, Map<String, Long> countByType,
                                    Map<String, BigDecimal> volumeByType, long totalAccounts,
                                    BigDecimal totalBalanceAcrossAllAccounts) {
        this.totalTransactions = totalTransactions;
        this.countByType = countByType;
        this.volumeByType = volumeByType;
        this.totalAccounts = totalAccounts;
        this.totalBalanceAcrossAllAccounts = totalBalanceAcrossAllAccounts;
    }

    public long getTotalTransactions() { return totalTransactions; }

    public Map<String, Long> getCountByType() { return countByType; }

    public Map<String, BigDecimal> getVolumeByType() { return volumeByType; }

    public long getTotalAccounts() { return totalAccounts; }

    public BigDecimal getTotalBalanceAcrossAllAccounts() { return totalBalanceAcrossAllAccounts; }
}
