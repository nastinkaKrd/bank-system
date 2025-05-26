package com.nastinka_krd.bank_system_mock_api.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceResponse {
    private String iban;

    private BigDecimal balance;

    private String currency;

    private LocalDateTime timestamp;
}
