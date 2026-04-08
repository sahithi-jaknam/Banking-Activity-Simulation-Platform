package com.bank.service;

import com.bank.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AlertService {

    private static final Logger log = LoggerFactory.getLogger(AlertService.class);

    private final JavaMailSender mailSender;

    @Value("${bank.alert.low-balance-threshold:1000}")
    private BigDecimal lowBalanceThreshold;

    @Value("${spring.mail.username:noreply@bank.com}")
    private String fromEmail;

    @Value("${bank.alert.email-enabled:false}")
    private boolean emailEnabled;

    public AlertService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void checkLowBalance(Account account) {
        if (account.getBalance().compareTo(lowBalanceThreshold) <= 0) {
            String subject = "Low Balance Alert - Account " + account.getAccountNumber();
            String body = String.format(
                    "Dear %s,%n%nYour account %s balance is low: $%.2f%n" +
                    "Please deposit funds to maintain minimum balance of $%.2f.%n%nRegards,%nBanking Simulation",
                    account.getHolderName(), account.getAccountNumber(),
                    account.getBalance(), lowBalanceThreshold);
            sendEmail(account.getEmail(), subject, body);
        }
    }

    private void sendEmail(String to, String subject, String body) {
        if (!emailEnabled) {
            log.info("[ALERT] Low balance email would be sent to {} — email disabled in config", to);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Low balance alert sent to {}", to);
        } catch (MailException ex) {
            log.warn("Failed to send low balance alert to {}: {}", to, ex.getMessage());
        }
    }
}
