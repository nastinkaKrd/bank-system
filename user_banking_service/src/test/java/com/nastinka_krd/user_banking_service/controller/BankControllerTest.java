package com.nastinka_krd.user_banking_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nastinka_krd.user_banking_service.domain.Currency;
import com.nastinka_krd.user_banking_service.domain.TransactionStatus;
import com.nastinka_krd.user_banking_service.dto.BalanceResponse;
import com.nastinka_krd.user_banking_service.dto.PaymentRequest;
import com.nastinka_krd.user_banking_service.dto.TransactionDto;
import com.nastinka_krd.user_banking_service.service.BankService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankControllerTest {
    private final String IBAN = "UA121234567890123456789012345";

    private MockMvc mockMvc;

    @Mock
    private BankService bankService;

    @InjectMocks
    private BankController bankController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bankController).build();
    }

    @Test
    void getBalance() throws Exception {
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setIban(IBAN);
        balanceResponse.setBalance(BigDecimal.valueOf(10000.00));
        balanceResponse.setCurrency(Currency.EUR);
        balanceResponse.setTimestamp(LocalDateTime.now());

        when(bankService.getBalance(IBAN)).thenReturn(balanceResponse);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/" + IBAN + "/balance")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.iban").value(IBAN));

        verify(bankService, times(1)).getBalance(IBAN);
    }

    @Test
    void getBalanceThrowValidationException() throws Exception {
        String invalidAccountId = "123";

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/{accountId}/balance", invalidAccountId)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


    @Test
    void getTransactions() throws Exception {
        List<TransactionDto> transactions = List.of(
                new TransactionDto(1, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(10.00), Currency.EUR, LocalDateTime.of(2025, 5, 23, 10, 0), TransactionStatus.COMPLETED),
                new TransactionDto(2, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), Currency.EUR, LocalDateTime.of(2025, 5, 22, 10, 0), TransactionStatus.COMPLETED),
                new TransactionDto(3, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(4.00), Currency.EUR, LocalDateTime.of(2025, 5, 21, 10, 0), TransactionStatus.COMPLETED));

        when(bankService.getTransactions(IBAN)).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/accounts/" + IBAN + "/transactions")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sourceIban").value(IBAN));

        verify(bankService, times(1)).getTransactions(IBAN);
    }

    @Test
    void initiatePayment() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setSourceIban(IBAN);
        paymentRequest.setAmount(BigDecimal.valueOf(100.00));
        paymentRequest.setCurrency("EUR");
        paymentRequest.setTargetIban("UA098765432109876543210987654");

        String message = "payment for user with iban:  " + paymentRequest.getSourceIban() + " is successful";

        when(bankService.initiatePayment(paymentRequest)).thenReturn(message);

        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/payments/initiate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest))
                        .accept(MediaType.TEXT_PLAIN))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(message));

        verify(bankService, times(1)).initiatePayment(paymentRequest);
    }

    @Test
    void getTransactionsFromLocalDb() throws Exception {
        List<TransactionDto> transactions = List.of(
                new TransactionDto(1, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(10.00), Currency.EUR, LocalDateTime.of(2025, 5, 23, 10, 0), TransactionStatus.COMPLETED),
                new TransactionDto(2, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(8.00), Currency.EUR, LocalDateTime.of(2025, 5, 22, 10, 0), TransactionStatus.COMPLETED),
                new TransactionDto(3, "UA121234567890123456789012345", "UA098765432109876543210987654",
                        BigDecimal.valueOf(4.00), Currency.EUR, LocalDateTime.of(2025, 5, 21, 10, 0), TransactionStatus.COMPLETED));

        when(bankService.getTransactionsFromLocalDb(IBAN)).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/" + IBAN + "/transactions/from-local-db")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sourceIban").value(IBAN));

        verify(bankService, times(1)).getTransactionsFromLocalDb(IBAN);
    }
}