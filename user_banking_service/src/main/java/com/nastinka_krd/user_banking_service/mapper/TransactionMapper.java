package com.nastinka_krd.user_banking_service.mapper;

import com.nastinka_krd.user_banking_service.domain.Transaction;
import com.nastinka_krd.user_banking_service.dto.TransactionDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    List<TransactionDto> transactionsToDtos(List<Transaction> transactions);
}
