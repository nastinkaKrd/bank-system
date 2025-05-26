package com.nastinka_krd.user_banking_service.service.impl;

import com.nastinka_krd.user_banking_service.domain.Currency;
import com.nastinka_krd.user_banking_service.domain.Transaction;
import com.nastinka_krd.user_banking_service.domain.TransactionStatus;
import com.nastinka_krd.user_banking_service.dto.BalanceResponse;
import com.nastinka_krd.user_banking_service.dto.PaymentRequest;
import com.nastinka_krd.user_banking_service.dto.PaymentResponse;
import com.nastinka_krd.user_banking_service.dto.TransactionDto;
import com.nastinka_krd.user_banking_service.exception.DataIsNotFoundException;
import com.nastinka_krd.user_banking_service.exception.NotEnoughMoneyException;
import com.nastinka_krd.user_banking_service.mapper.TransactionMapper;
import com.nastinka_krd.user_banking_service.repository.TransactionRepository;
import com.nastinka_krd.user_banking_service.service.BankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Log4j2
public class BankServiceImpl implements BankService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final RestTemplate restTemplate;
    private final String mockBankApiUrl = "http://localhost:8081/mock-bank-api";

    @Override
    public BalanceResponse getBalance(String accountId) {
        String url = mockBankApiUrl + "/accounts/" + accountId + "/balance";
        BalanceResponse balanceResponse = restTemplate.getForObject(url, BalanceResponse.class);
        if (balanceResponse == null) {
            log.error("Balance not found for account {}", accountId);
            throw new DataIsNotFoundException("Account is not found");
        }
        log.info("Balance is found: {}", balanceResponse);
        return balanceResponse;
    }

    @Override
    public List<TransactionDto> getTransactions(String accountId) {
        String url = mockBankApiUrl + "/accounts/" + accountId + "/transactions";
        List<TransactionDto> transactions = Arrays.asList(Objects.requireNonNull(restTemplate.getForObject(url, TransactionDto[].class)));
        if (transactions.isEmpty()) {
            log.warn("Transactions are not found for account {}", accountId);
            throw new DataIsNotFoundException("Transactions are not found");
        }
        log.info("Transactions are found!");
        int size = transactions.size();
        transactions.sort(Comparator.comparing(TransactionDto::getTimestamp));
        return transactions.subList(Math.max(size - 10, 0), size);
    }

    @Override
    public String initiatePayment(PaymentRequest paymentRequest) {
        String url = mockBankApiUrl + "/payments/initiate";

        Transaction transaction = new Transaction();
        transaction.setAmount(paymentRequest.getAmount());
        transaction.setCurrency(Currency.valueOf(paymentRequest.getCurrency()));
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setSourceIban(paymentRequest.getSourceIban());
        transaction.setTargetIban(paymentRequest.getTargetIban());
        transaction.setStatus(TransactionStatus.PENDING);
        transactionRepository.save(transaction);

        PaymentResponse paymentResponse = restTemplate.postForObject(url, paymentRequest, PaymentResponse.class);
        assert paymentResponse != null;

        transaction.setStatus(paymentResponse.getStatus());
        transaction.setTimestamp(paymentResponse.getTimestamp());
        transactionRepository.save(transaction);

        if (paymentResponse.getStatus().equals(TransactionStatus.FAILED)){
            log.error("Payment is failed");
            throw new NotEnoughMoneyException("Not enough money to initiate this transaction");
        }

        log.info("Payment is successful");

        return paymentResponse.getMessage();
    }

    @Override
    public List<TransactionDto> getTransactionsFromLocalDb(String accountId) {
        List<Transaction> transactions = transactionRepository.getTransactionsBySourceIban(accountId);
        if (transactions.isEmpty()) {
            log.error("Transactions from local db are not found for account {}", accountId);
            throw new DataIsNotFoundException("Transactions are not found");
        }
        log.info("Transactions from local db are found!");
        return transactionMapper.transactionsToDtos(transactions);
    }
}
