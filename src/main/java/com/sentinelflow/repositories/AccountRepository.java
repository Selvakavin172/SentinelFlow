package com.sentinelflow.repositories;

import com.sentinelflow.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumber(@Param("accountNumber") String accountNumber);

    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId")
    List<Account> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT a FROM Account a WHERE a.status = :status")
    List<Account> findByStatus(@Param("status") String status);

    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId AND a.status = :status")
    List<Account> findByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") String status);

    @Query("SELECT a FROM Account a WHERE a.isActive = true AND a.currency = :currency")
    List<Account> findByIsActiveTrueAndCurrency(@Param("currency") String currency);

    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId AND a.isActive = true")
    List<Account> findActiveAccountsByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT a FROM Account a WHERE a.status = 'ACTIVE' AND a.currentBalance > :minBalance")
    List<Account> findActiveAccountsWithMinBalance(@Param("minBalance") BigDecimal minBalance);

    @Query("SELECT a FROM Account a WHERE a.openingDate >= :startDate AND a.openingDate <= :endDate")
    List<Account> findAccountsOpenedBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT a FROM Account a WHERE a.customer.customerId = :customerId AND a.currency = :currency AND a.isActive = true")
    List<Account> findActiveAccountsByCustomerAndCurrency(@Param("customerId") Long customerId, @Param("currency") String currency);

    @Query("SELECT a FROM Account a WHERE a.jointAccount = true AND a.status = 'ACTIVE'")
    List<Account> findActiveJointAccounts();
}
