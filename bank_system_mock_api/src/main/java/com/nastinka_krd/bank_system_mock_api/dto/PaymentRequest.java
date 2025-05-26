package com.nastinka_krd.bank_system_mock_api.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String sourceIban;

    private String targetIban;

    private BigDecimal amount;

    private String currency;
}

