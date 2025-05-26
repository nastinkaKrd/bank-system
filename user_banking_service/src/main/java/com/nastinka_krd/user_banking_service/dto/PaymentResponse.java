package com.nastinka_krd.user_banking_service.dto;

import com.nastinka_krd.user_banking_service.domain.TransactionStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PaymentResponse {
    private TransactionStatus status;

    private LocalDateTime timestamp;

    private String message;
}
