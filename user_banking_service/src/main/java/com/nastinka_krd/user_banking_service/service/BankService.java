package com.nastinka_krd.user_banking_service.service;

import com.nastinka_krd.user_banking_service.dto.BalanceResponse;
import com.nastinka_krd.user_banking_service.dto.PaymentRequest;
import com.nastinka_krd.user_banking_service.dto.TransactionDto;

import java.util.List;

public interface BankService {
    BalanceResponse getBalance(String accountId);

    List<TransactionDto> getTransactions(String accountId);

    String initiatePayment(PaymentRequest paymentRequest);

    List<TransactionDto> getTransactionsFromLocalDb(String accountId);
}
