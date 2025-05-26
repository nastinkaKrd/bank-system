package com.nastinka_krd.user_banking_service.repository;

import com.nastinka_krd.user_banking_service.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> getTransactionsBySourceIban(String sourceIban);
}
