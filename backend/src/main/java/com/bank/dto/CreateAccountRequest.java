package com.bank.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateAccountRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Opening balance is required")
    @DecimalMin(value = "0.00", message = "Opening balance cannot be negative")
    private BigDecimal openingBalance;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public BigDecimal getOpeningBalance() { return openingBalance; }

    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }
}
