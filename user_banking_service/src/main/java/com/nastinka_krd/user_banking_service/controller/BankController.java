package com.nastinka_krd.user_banking_service.controller;

import com.nastinka_krd.user_banking_service.dto.BalanceResponse;
import com.nastinka_krd.user_banking_service.dto.ErrorResponse;
import com.nastinka_krd.user_banking_service.dto.PaymentRequest;
import com.nastinka_krd.user_banking_service.dto.TransactionDto;
import com.nastinka_krd.user_banking_service.service.BankService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@OpenAPIDefinition(
        info = @Info(
                title = "Bank Controller",
                version = "1.0",
                description = "Controller that processes users' bank information."
        )
)
public class BankController {
    private final BankService bankService;

    @GetMapping("/accounts/{accountId}/balance")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return balance info for user iban.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Balance info is returned.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account is not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Iban value is not right.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public BalanceResponse getBalance(@PathVariable("accountId") @Pattern(regexp = "^UA\\d{2}\\d{6}\\d{19}$") String accountId) {
        return bankService.getBalance(accountId);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return user transactions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions are returned.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "404", description = "Transactions are not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Iban value is not right.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<TransactionDto> getTransactions(@PathVariable("accountId") @Pattern(regexp = "^UA\\d{2}\\d{6}\\d{19}$") String accountId) {
        return bankService.getTransactions(accountId);
    }

    @PostMapping("/payments/initiate")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Initiate payment.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Payment is initiated.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BalanceResponse.class))),
            @ApiResponse(responseCode = "409", description = "Not enough money to initiate this transaction.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation exception because of not right values of request body.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public String initiatePayment(@RequestBody @Valid PaymentRequest paymentRequest) {
        return bankService.initiatePayment(paymentRequest);
    }


    @GetMapping("/accounts/{accountId}/transactions/from-local-db")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Return user transactions.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transactions are returned.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionDto.class))),
            @ApiResponse(responseCode = "404", description = "Transactions are not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Iban value is not right.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    public List<TransactionDto> getTransactionsFromLocalDb(@PathVariable("accountId") @Pattern(regexp = "^UA\\d{2}\\d{6}\\d{19}$") String accountId) {
        return bankService.getTransactionsFromLocalDb(accountId);
    }
}
