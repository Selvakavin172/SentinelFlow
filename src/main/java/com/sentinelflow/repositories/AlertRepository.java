package com.sentinelflow.repositories;

import com.sentinelflow.models.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {

    @Query("SELECT a FROM Alert a WHERE a.customer.customerId = :customerId")
    List<Alert> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT a FROM Alert a WHERE a.account.accountId = :accountId")
    List<Alert> findByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT a FROM Alert a WHERE a.transaction.transactionId = :transactionId")
    List<Alert> findByTransactionId(@Param("transactionId") Long transactionId);

    @Query("SELECT a FROM Alert a WHERE a.alertStatus = :alertStatus")
    List<Alert> findByAlertStatus(@Param("alertStatus") String alertStatus);

    @Query("SELECT a FROM Alert a WHERE a.ruleCode = :ruleCode")
    List<Alert> findByRuleCode(@Param("ruleCode") String ruleCode);

    @Query("SELECT a FROM Alert a WHERE a.alertStatus = 'OPEN' ORDER BY a.riskScore DESC, a.triggeredAt DESC")
    List<Alert> findOpenAlertsSortedByRiskScore();

    @Query("SELECT a FROM Alert a WHERE a.customer.customerId = :customerId AND a.alertStatus = 'OPEN' ORDER BY a.riskScore DESC")
    List<Alert> findOpenAlertsByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT a FROM Alert a WHERE a.riskScore >= :minScore AND a.triggeredAt BETWEEN :startTime AND :endTime ORDER BY a.riskScore DESC")
    List<Alert> findAlertsByRiskScoreAndTimeRange(@Param("minScore") Integer minScore, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.alertStatus = 'OPEN'")
    Long countOpenAlerts();

    @Query("SELECT COUNT(a) FROM Alert a WHERE a.customer.customerId = :customerId AND a.alertStatus = 'OPEN'")
    Long countOpenAlertsByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT a FROM Alert a WHERE a.ruleCode = :ruleCode AND a.customer.customerId = :customerId AND a.alertStatus = 'OPEN'")
    List<Alert> findOpenAlertsByRuleCodeAndCustomer(@Param("ruleCode") String ruleCode, @Param("customerId") Long customerId);

    @Query("SELECT a FROM Alert a WHERE a.alertStatus = :alertStatus AND a.createdBy = :createdBy ORDER BY a.triggeredAt DESC")
    List<Alert> findByAlertStatusAndCreatedByOrderByTriggeredAtDesc(@Param("alertStatus") String alertStatus, @Param("createdBy") String createdBy);

    @Query("SELECT a FROM Alert a WHERE a.riskScore > :threshold AND a.alertStatus = 'OPEN' ORDER BY a.riskScore DESC")
    List<Alert> findCriticalAlerts(@Param("threshold") Integer threshold);

    @Query("SELECT a FROM Alert a WHERE a.triggeredAt BETWEEN :startTime AND :endTime ORDER BY a.triggeredAt DESC")
    List<Alert> findAlertsByDateRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
