package com.nastinka_krd.bank_system_mock_api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private String status;

    private LocalDateTime timestamp;

    private String message;
}
