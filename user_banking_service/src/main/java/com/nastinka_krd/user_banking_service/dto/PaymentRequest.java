package com.nastinka_krd.user_banking_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    @NotBlank
    @Pattern(regexp = "^UA\\d{2}\\d{6}\\d{19}$")
    private String sourceIban;

    @NotBlank
    @Pattern(regexp = "^UA\\d{2}\\d{6}\\d{19}$")
    private String targetIban;

    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotBlank
    private String currency;
}
