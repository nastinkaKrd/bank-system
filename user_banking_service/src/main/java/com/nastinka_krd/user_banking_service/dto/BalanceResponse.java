package com.nastinka_krd.user_banking_service.dto;

import com.nastinka_krd.user_banking_service.domain.Currency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BalanceResponse {
    private String iban;

    private BigDecimal balance;

    private Currency currency;

    private LocalDateTime timestamp;
}
