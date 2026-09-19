package com.sentinelflow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction", indexes = {
    @Index(name = "idx_transaction_account", columnList = "account_id"),
    @Index(name = "idx_transaction_datetime", columnList = "transaction_datetime"),
    @Index(name = "idx_transaction_country", columnList = "country_code"),
    @Index(name = "idx_transaction_amount", columnList = "amount")
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    @Column(name = "transaction_id")
    private Long transactionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "counterparty_account", length = 50)
    private String counterpartyAccount;

    @Column(name = "counterparty_name", length = 150)
    private String counterpartyName;

    @Column(name = "transaction_type", nullable = false, length = 50)
    private String transactionType;

    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 10)
    private String currency;

    @Column(name = "amount_inr", precision = 18, scale = 2)
    private BigDecimal amountInr;

    @Column(name = "transaction_datetime", nullable = false)
    private LocalDateTime transactionDatetime;

    @Column(name = "country_code", length = 10)
    private String countryCode;

    @Column(name = "channel", length = 50)
    private String channel;

    @Column(name = "merchant_category", length = 100)
    private String merchantCategory;

    @Column(name = "narration", columnDefinition = "TEXT")
    private String narration;

    @Column(name = "is_high_risk")
    private Boolean isHighRisk;

    @Column(name = "status", length = 30)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (isHighRisk == null) {
            isHighRisk = false;
        }

        if (status == null) {
            status = "COMPLETED";
        }
    }

    // Getters

    public Long getTransactionId() {
        return transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public String getCounterpartyAccount() {
        return counterpartyAccount;
    }

    public String getCounterpartyName() {
        return counterpartyName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmountInr() {
        return amountInr;
    }

    public LocalDateTime getTransactionDatetime() {
        return transactionDatetime;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getChannel() {
        return channel;
    }

    public String getMerchantCategory() {
        return merchantCategory;
    }

    public String getNarration() {
        return narration;
    }

    public Boolean getIsHighRisk() {
        return isHighRisk;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setCounterpartyAccount(String counterpartyAccount) {
        this.counterpartyAccount = counterpartyAccount;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName = counterpartyName;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAmountInr(BigDecimal amountInr) {
        this.amountInr = amountInr;
    }

    public void setTransactionDatetime(LocalDateTime transactionDatetime) {
        this.transactionDatetime = transactionDatetime;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setMerchantCategory(String merchantCategory) {
        this.merchantCategory = merchantCategory;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public void setIsHighRisk(Boolean isHighRisk) {
        this.isHighRisk = isHighRisk;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}