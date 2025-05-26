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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankServiceImplTest {

    private final String IBAN = "UA121234567890123456789012345";
    private final String MOCK_URL = "http://localhost:8081/mock-bank-api";

    @Mock
    private TransactionMapper transactionMapper;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private BankServiceImpl bankService;


    @Test
    void getBalance() {
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setIban(IBAN);
        balanceResponse.setBalance(BigDecimal.valueOf(10000.00));
        balanceResponse.setCurrency(Currency.EUR);
        balanceResponse.setTimestamp(LocalDateTime.now());

        String url = MOCK_URL + "/accounts/" + IBAN + "/balance";

        when(restTemplate.getForObject(url, BalanceResponse.class)).thenReturn(balanceResponse);

        BalanceResponse testBalanceResponse = bankService.getBalance(IBAN);

        assertEquals(balanceResponse.getIban(), testBalanceResponse.getIban());

        verify(restTemplate, times(1)).getForObject(url, BalanceResponse.class);
    }

    @Test
    void getBalanceThrowDataIsNotFoundException() {
        String url = MOCK_URL + "/accounts/" + IBAN + "/balance";
        when(restTemplate.getForObject(url, BalanceResponse.class)).thenReturn(null);

        DataIsNotFoundException exception = assertThrows(
                DataIsNotFoundException.class,
                () -> bankService.getBalance(IBAN)
        );

        assertEquals("Account is not found", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, BalanceResponse.class);
    }


    @Test
    void getTransactions() {
        List<TransactionDto> transactions = List.of(
                new TransactionDto(1, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(10.00), Currency.EUR, LocalDateTime.of(2025, 5, 23, 10, 0), TransactionStatus.COMPLETED),
                new TransactionDto(2, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), Currency.EUR, LocalDateTime.of(2025, 5, 22, 10, 0), TransactionStatus.COMPLETED),
                new TransactionDto(3, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(4.00), Currency.EUR, LocalDateTime.of(2025, 5, 21, 10, 0), TransactionStatus.COMPLETED));

        String url = MOCK_URL + "/accounts/" + IBAN + "/transactions";

        when(restTemplate.getForObject(url, TransactionDto[].class)).thenReturn(transactions.toArray(new TransactionDto[0]));

        List<TransactionDto> testTransactions = bankService.getTransactions(IBAN);

        assertEquals(transactions.size(), testTransactions.size());

        verify(restTemplate, times(1)).getForObject(url, TransactionDto[].class);
    }

    @Test
    void getTransactionsThrowDataIsNotFoundException() {
        String url = MOCK_URL + "/accounts/" + IBAN + "/transactions";
        when(restTemplate.getForObject(url, TransactionDto[].class)).thenReturn(new TransactionDto[0]);

        DataIsNotFoundException exception = assertThrows(
                DataIsNotFoundException.class,
                () -> bankService.getTransactions(IBAN)
        );

        assertEquals("Transactions are not found", exception.getMessage());
        verify(restTemplate, times(1)).getForObject(url, TransactionDto[].class);
    }

    @Test
    void initiatePayment() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setSourceIban(IBAN);
        paymentRequest.setAmount(BigDecimal.valueOf(100.00));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setTargetIban("UA098765432109876543210987654");

        String message = "payment for user with iban:  " + paymentRequest.getSourceIban() + " is successful";
        String url = MOCK_URL + "/payments/initiate";
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(TransactionStatus.COMPLETED);
        paymentResponse.setTimestamp(LocalDateTime.now());
        paymentResponse.setMessage(message);

        when(restTemplate.postForObject(url, paymentRequest, PaymentResponse.class)).thenReturn(paymentResponse);

        String testMessage = bankService.initiatePayment(paymentRequest);

        assertEquals(message, testMessage);

        verify(restTemplate, times(1)).postForObject(url, paymentRequest, PaymentResponse.class);
    }

    @Test
    void initiatePaymentThrowNotEnoughMoneyException() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setSourceIban(IBAN);
        paymentRequest.setAmount(BigDecimal.valueOf(100.00));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setTargetIban("UA098765432109876543210987654");

        String url = MOCK_URL + "/payments/initiate";
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus(TransactionStatus.FAILED);

        when(restTemplate.postForObject(url, paymentRequest, PaymentResponse.class)).thenReturn(paymentResponse);

        NotEnoughMoneyException exception = assertThrows(
                NotEnoughMoneyException.class,
                () -> bankService.initiatePayment(paymentRequest)
        );

        assertEquals("Not enough money to initiate this transaction", exception.getMessage());

        verify(restTemplate, times(1)).postForObject(url, paymentRequest, PaymentResponse.class);
    }

    @Test
    void getTransactionsFromLocalDb() {
        List<Transaction> transactions = List.of(
                new Transaction(1, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(10.00), Currency.EUR, LocalDateTime.of(2025, 5, 23, 10, 0), TransactionStatus.COMPLETED),
                new Transaction(2, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), Currency.EUR, LocalDateTime.of(2025, 5, 22, 10, 0), TransactionStatus.COMPLETED),
                new Transaction(3, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(4.00), Currency.EUR, LocalDateTime.of(2025, 5, 21, 10, 0), TransactionStatus.COMPLETED));

        List<TransactionDto> transactionsDtos = List.of(
                new TransactionDto(1, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(10.00), Currency.EUR, LocalDateTime.of(2025, 5, 23, 10, 0), TransactionStatus.COMPLETED),
                new TransactionDto(2, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), Currency.EUR, LocalDateTime.of(2025, 5, 22, 10, 0), TransactionStatus.COMPLETED),
                new TransactionDto(3, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(4.00), Currency.EUR, LocalDateTime.of(2025, 5, 21, 10, 0), TransactionStatus.COMPLETED));

        when(transactionRepository.getTransactionsBySourceIban(IBAN)).thenReturn(transactions);

        when(transactionMapper.transactionsToDtos(transactions)).thenReturn(transactionsDtos);

        List<TransactionDto> testTransactions = bankService.getTransactionsFromLocalDb(IBAN);

        assertEquals(transactions.size(), testTransactions.size());

        verify(transactionRepository, times(1)).getTransactionsBySourceIban(IBAN);
    }


    @Test
    void getTransactionsFromLocalDbThrowDataIsNotFoundException() {
        when(transactionRepository.getTransactionsBySourceIban(IBAN)).thenReturn(Collections.emptyList());

        DataIsNotFoundException exception = assertThrows(
                DataIsNotFoundException.class,
                () -> bankService.getTransactionsFromLocalDb(IBAN)
        );

        assertEquals("Transactions are not found", exception.getMessage());
        verify(transactionRepository, times(1)).getTransactionsBySourceIban(IBAN);
    }
}