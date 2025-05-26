package com.nastinka_krd.bank_system_mock_api.dto;

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

    private String currency;

    private LocalDateTime timestamp;

    private String status;
}
