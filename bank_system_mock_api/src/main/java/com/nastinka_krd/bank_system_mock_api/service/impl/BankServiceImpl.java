package com.nastinka_krd.bank_system_mock_api.service.impl;

import com.nastinka_krd.bank_system_mock_api.dto.BalanceResponse;
import com.nastinka_krd.bank_system_mock_api.dto.PaymentRequest;
import com.nastinka_krd.bank_system_mock_api.dto.PaymentResponse;
import com.nastinka_krd.bank_system_mock_api.dto.TransactionDto;
import com.nastinka_krd.bank_system_mock_api.service.BankService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BankServiceImpl implements BankService {

    @Override
    public BalanceResponse getBalance(String accountId) {
        if (accountId == null) {
            throw new IllegalArgumentException("Account ID cannot be null");
        }
        BalanceResponse balanceResponse = new BalanceResponse();
        if (accountId.equals("UA121234567890123456789012345")){
            balanceResponse.setIban(accountId);
            balanceResponse.setBalance(BigDecimal.valueOf(10000.00));
            balanceResponse.setCurrency("EUR");
            balanceResponse.setTimestamp(LocalDateTime.now());
            return balanceResponse;
        }else if (accountId.equals("UA098765432109876543210987654")){
            balanceResponse.setIban(accountId);
            balanceResponse.setBalance(BigDecimal.valueOf(80000.45));
            balanceResponse.setCurrency("EUR");
            balanceResponse.setTimestamp(LocalDateTime.now());
            return balanceResponse;
        }
        return null;
    }

    @Override
    public List<TransactionDto> getTransactions(String accountId) {
        return List.of(
                new TransactionDto(1, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(10.00), "EUR", LocalDateTime.of(2025, 5, 23, 10, 0), "COMPLETED"),
                new TransactionDto(2, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), "EUR", LocalDateTime.of(2025, 5, 22, 10, 0), "COMPLETED"),
                new TransactionDto(3, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(4.00), "EUR", LocalDateTime.of(2025, 5, 21, 10, 0), "COMPLETED"),
                new TransactionDto(4, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(9.00), "EUR", LocalDateTime.of(2025, 5, 20, 10, 0), "COMPLETED"),
                new TransactionDto(5, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(15.00), "EUR", LocalDateTime.of(2025, 5, 15, 10, 0), "COMPLETED"),
                new TransactionDto(6, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(16.00), "EUR", LocalDateTime.of(2025, 5, 13, 10, 0), "FAILED"),
                new TransactionDto(7, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(14.00), "EUR", LocalDateTime.of(2025, 5, 14, 10, 0), "COMPLETED"),
                new TransactionDto(8, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(12.00), "EUR", LocalDateTime.of(2025, 5, 9, 10, 0), "FAILED"),
                new TransactionDto(9, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(2.00), "EUR", LocalDateTime.of(2025, 5, 8, 10, 0), "COMPLETED"),
                new TransactionDto(10, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), "EUR", LocalDateTime.of(2025, 5, 10, 10, 0), "COMPLETED"),
                new TransactionDto(11, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), "EUR", LocalDateTime.of(2025, 5, 5, 10, 0), "COMPLETED"),
                new TransactionDto(12, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), "EUR", LocalDateTime.of(2025, 5, 3, 10, 0), "COMPLETED")
        );
    }

    @Override
    public PaymentResponse initiatePayment(PaymentRequest paymentRequest) {
        BalanceResponse balanceResponse = getBalance(paymentRequest.getSourceIban());
        PaymentResponse paymentResponse = new PaymentResponse();
        if (balanceResponse == null || balanceResponse.getBalance().subtract(paymentRequest.getAmount()).compareTo(BigDecimal.ZERO) < 0) {
            paymentResponse.setStatus("FAILED");
            paymentResponse.setTimestamp(LocalDateTime.now());
            paymentResponse.setMessage("payment for user with iban:  " + paymentRequest.getSourceIban() + " is failed");
        }else {
            paymentResponse.setStatus("COMPLETED");
            paymentResponse.setTimestamp(LocalDateTime.now());
            paymentResponse.setMessage("payment for user with iban:  " + paymentRequest.getSourceIban() + " is successful");
        }
        return paymentResponse;
    }
}
