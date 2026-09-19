package com.sentinelflow.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account", indexes = {
    @Index(name = "idx_account_number", columnList = "account_number"),
    @Index(name = "idx_account_customer", columnList = "customer_id"),
    @Index(name = "idx_account_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_id_seq", allocationSize = 1)
    @Column(name = "account_id")
    private Long accountId; 

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @Column(name = "account_number", unique = true, nullable = false, length = 50)
    private String accountNumber;

    @Column(name = "account_type", nullable = false, length = 50)
    private String accountType;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "opening_date", nullable = false)
    private LocalDate openingDate;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "branch_code", length = 30)
    private String branchCode;

    @Column(name = "current_balance", precision = 18, scale = 2)
    private BigDecimal currentBalance;

    @Column(name = "avg_monthly_credit", precision = 18, scale = 2)
    private BigDecimal avgMonthlyCredit;

    @Column(name = "avg_monthly_debit", precision = 18, scale = 2)
    private BigDecimal avgMonthlyDebit;

    @Column(name = "credit_limit", precision = 18, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "overdraft_limit", precision = 18, scale = 2)
    private BigDecimal overdraftLimit;

    @Column(name = "card_type", length = 50)
    private String cardType;

    @Column(name = "joint_account")
    private Boolean jointAccount;

    @Column(name = "linked_mobile", length = 30)
    private String linkedMobile;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Alert> alerts;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
        if (status == null) status = "ACTIVE";
        if (currentBalance == null) currentBalance = BigDecimal.ZERO;
        if (avgMonthlyCredit == null) avgMonthlyCredit = BigDecimal.ZERO;
        if (avgMonthlyDebit == null) avgMonthlyDebit = BigDecimal.ZERO;
        if (jointAccount == null) jointAccount = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

 

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(LocalDate openingDate) {
        this.openingDate = openingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getAvgMonthlyCredit() {
        return avgMonthlyCredit;
    }

    public void setAvgMonthlyCredit(BigDecimal avgMonthlyCredit) {
        this.avgMonthlyCredit = avgMonthlyCredit;
    }

    public BigDecimal getAvgMonthlyDebit() {
        return avgMonthlyDebit;
    }

    public void setAvgMonthlyDebit(BigDecimal avgMonthlyDebit) {
        this.avgMonthlyDebit = avgMonthlyDebit;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(BigDecimal overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Boolean getJointAccount() {
        return jointAccount;
    }

    public void setJointAccount(Boolean jointAccount) {
        this.jointAccount = jointAccount;
    }

    public String getLinkedMobile() {
        return linkedMobile;
    }

    public void setLinkedMobile(String linkedMobile) {
        this.linkedMobile = linkedMobile;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }
}
