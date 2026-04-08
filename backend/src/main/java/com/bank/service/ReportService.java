package com.bank.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Value("${bank.report.file:reports/transactions.txt}")
    private String reportFile;

    public void log(String type, String accountNumber, double amount, String targetAccountNumber) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String target = targetAccountNumber != null ? " | To: " + targetAccountNumber : "";
        String line = String.format("[%s] %s | Acc: %s | Amount: %.2f%s",
                timestamp, type, accountNumber, amount, target);
        writeLine(line);
    }

    private void writeLine(String line) {
        try {
            File file = new File(reportFile);
            file.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException ex) {
            log.warn("Could not write transaction report: {}", ex.getMessage());
        }
    }
}
