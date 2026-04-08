package com.bank.repository;

import com.bank.model.Transaction;
import com.bank.model.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount_AccountNumberOrderByTimestampDesc(String accountNumber);

    Page<Transaction> findByAccount_AccountNumberOrderByTimestampDesc(String accountNumber, Pageable pageable);

    long countByType(TransactionType type);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = :type")
    BigDecimal sumAmountByType(TransactionType type);
}
