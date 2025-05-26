package com.nastinka_krd.bank_system_mock_api.service;

import com.nastinka_krd.bank_system_mock_api.dto.BalanceResponse;
import com.nastinka_krd.bank_system_mock_api.dto.PaymentRequest;
import com.nastinka_krd.bank_system_mock_api.dto.PaymentResponse;
import com.nastinka_krd.bank_system_mock_api.dto.TransactionDto;

import java.util.List;

public interface BankService {
    BalanceResponse getBalance(String accountId);

    List<TransactionDto> getTransactions(String accountId);

    PaymentResponse initiatePayment(PaymentRequest paymentRequest);
}
