package com.nastinka_krd.bank_system_mock_api.controller;

import com.nastinka_krd.bank_system_mock_api.dto.BalanceResponse;
import com.nastinka_krd.bank_system_mock_api.dto.PaymentRequest;
import com.nastinka_krd.bank_system_mock_api.dto.PaymentResponse;
import com.nastinka_krd.bank_system_mock_api.dto.TransactionDto;
import com.nastinka_krd.bank_system_mock_api.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mock-bank-api")
public class BankController {
    private final BankService bankService;

    @GetMapping("/accounts/{accountId}/balance")
    public BalanceResponse getBalance(@PathVariable("accountId") String accountId) {
        return bankService.getBalance(accountId);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<TransactionDto> getTransactions(@PathVariable("accountId") String accountId) {
        return bankService.getTransactions(accountId);
    }

    @PostMapping("/payments/initiate")
    public PaymentResponse initiatePayment(@RequestBody PaymentRequest paymentRequest) {
        return bankService.initiatePayment(paymentRequest);
    }
}
