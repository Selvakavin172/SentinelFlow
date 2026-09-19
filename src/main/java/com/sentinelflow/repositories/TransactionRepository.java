package com.sentinelflow.repositories;

import com.sentinelflow.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	@Query("""
		    SELECT COALESCE(SUM(t.amountInr), 0)
		    FROM Transaction t
		    WHERE t.account.accountId = :accountId
		    AND t.transactionDatetime BETWEEN :start AND :end
		""")
		BigDecimal calculateAccountTransactionTotal(
		        @Param("accountId") Long accountId,
		        @Param("start") LocalDateTime start,
		        @Param("end") LocalDateTime end
		);

		@Query("""
		    SELECT COALESCE(SUM(t.amountInr), 0)
		    FROM Transaction t
		    WHERE t.account.accountId = :accountId
		    AND t.transactionDatetime BETWEEN :start AND :end
		""")
		BigDecimal calculateAccountTransactionTotalFor90Days(
		        @Param("accountId") Long accountId,
		        @Param("start") LocalDateTime start,
		        @Param("end") LocalDateTime end
		);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId")
    List<Transaction> findByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.status = :status")
    List<Transaction> findByAccountIdAndStatus(@Param("accountId") Long accountId, @Param("status") String status);

    @Query("SELECT t FROM Transaction t WHERE t.countryCode = :countryCode")
    List<Transaction> findByCountryCode(@Param("countryCode") String countryCode);

    @Query("SELECT t FROM Transaction t WHERE t.isHighRisk = true")
    List<Transaction> findByIsHighRiskTrue();

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionDatetime BETWEEN :startTime AND :endTime ORDER BY t.transactionDatetime DESC")
    List<Transaction> findTransactionsByAccountAndTimeRange(@Param("accountId") Long accountId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT t FROM Transaction t WHERE t.amount >= :minAmount AND t.amount <= :maxAmount AND t.transactionDatetime BETWEEN :startTime AND :endTime")
    List<Transaction> findTransactionsByAmountAndTimeRange(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.amount >= :amount AND t.transactionDatetime BETWEEN :startTime AND :endTime ORDER BY t.transactionDatetime DESC")
    List<Transaction> findHighValueTransactionsByAccountAndTimeRange(@Param("accountId") Long accountId, @Param("amount") BigDecimal amount, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT SUM(t.amountInr) as totalAmount FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionDatetime BETWEEN :startTime AND :endTime")
    BigDecimal sumTransactionAmountByAccountAndTimeRange(@Param("accountId") Long accountId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT SUM(t.amountInr) FROM Transaction t WHERE t.account.customer.customerId = :customerId AND t.transactionDatetime BETWEEN :startTime AND :endTime")
    BigDecimal sumTransactionAmountByCustomerAndTimeRange(@Param("customerId") Long customerId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT t FROM Transaction t WHERE t.channel = :channel AND t.isHighRisk = true ORDER BY t.transactionDatetime DESC")
    List<Transaction> findHighRiskTransactionsByChannel(@Param("channel") String channel);

    @Query("SELECT t FROM Transaction t WHERE t.countryCode = :countryCode AND t.status = :status AND t.transactionDatetime BETWEEN :startTime AND :endTime")
    List<Transaction> findByCountryCodeAndStatusAndTransactionDatetimeBetween(@Param("countryCode") String countryCode, @Param("status") String status, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionDatetime BETWEEN :startTime AND :endTime")
    Long countTransactionsByAccountAndTimeRange(@Param("accountId") Long accountId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionType = :type AND t.transactionDatetime BETWEEN :startTime AND :endTime ORDER BY t.transactionDatetime DESC")
    List<Transaction> findTransactionsByTypeAndTimeRange(@Param("accountId") Long accountId, @Param("type") String type, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
