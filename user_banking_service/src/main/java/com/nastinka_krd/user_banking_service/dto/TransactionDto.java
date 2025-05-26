package com.nastinka_krd.user_banking_service.dto;

import com.nastinka_krd.user_banking_service.domain.Currency;
import com.nastinka_krd.user_banking_service.domain.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionDto {
    private Integer id;

    private String sourceIban;

    private String targetIban;

    private BigDecimal amount;

    private Currency currency;

    private LocalDateTime timestamp;

    private TransactionStatus status;
}
