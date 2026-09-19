package com.sentinelflow.models;

import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alert", indexes = {
    @Index(name = "idx_alert_customer", columnList = "customer_id"),
    @Index(name = "idx_alert_account", columnList = "account_id"),
    @Index(name = "idx_alert_status", columnList = "alert_status"),
    @Index(name = "idx_alert_risk_score", columnList = "risk_score"),
    @Index(name = "idx_alert_triggered", columnList = "triggered_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "alert_seq")
    @SequenceGenerator(name = "alert_seq", sequenceName = "alert_id_seq", allocationSize = 1)
    @Column(name = "alert_id")
    private Long alertId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "rule_code", nullable = false, length = 50)
    private String ruleCode;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Column(name = "risk_score", nullable = false)
    private Integer riskScore;

    @Column(name = "alert_status", length = 30)
    private String alertStatus;

    @Column(name = "alert_reason", columnDefinition = "TEXT")
    private String alertReason;

    @Column(name = "evidence_json", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String evidenceJson;

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @PrePersist
    protected void onCreate() {
        if (triggeredAt == null) {
            triggeredAt = LocalDateTime.now();
        }
        if (alertStatus == null) {
            alertStatus = "OPEN";
        }
        if (createdBy == null) {
            createdBy = "SYSTEM";
        }
    }
    public Long getAlertId() {
        return alertId;
    }

    public void setAlertId(Long alertId) {
        this.alertId = alertId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public void setRuleCode(String ruleCode) {
        this.ruleCode = ruleCode;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public String getAlertStatus() {
        return alertStatus;
    }

    public void setAlertStatus(String alertStatus) {
        this.alertStatus = alertStatus;
    }

    public String getAlertReason() {
        return alertReason;
    }

    public void setAlertReason(String alertReason) {
        this.alertReason = alertReason;
    }

    public String getEvidenceJson() {
        return evidenceJson;
    }

    public void setEvidenceJson(String evidenceJson) {
        this.evidenceJson = evidenceJson;
    }

    public LocalDateTime getTriggeredAt() {
        return triggeredAt;
    }

    public void setTriggeredAt(LocalDateTime triggeredAt) {
        this.triggeredAt = triggeredAt;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
