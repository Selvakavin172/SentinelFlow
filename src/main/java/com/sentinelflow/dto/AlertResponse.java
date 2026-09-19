package com.sentinelflow.dto;

import java.time.LocalDateTime;

public class AlertResponse {

    private Long alertId;
    private Long customerId;
    private Long accountId;
    private Long transactionId;
    private String ruleCode;
    private String ruleName;
    private Integer riskScore;
    private String alertStatus;
    private String alertReason;
    private LocalDateTime triggeredAt;
    private String createdBy;

    public Long getAlertId() { return alertId; }
    public void setAlertId(Long alertId) { this.alertId = alertId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public String getRuleCode() { return ruleCode; }
    public void setRuleCode(String ruleCode) { this.ruleCode = ruleCode; }

    public String getRuleName() { return ruleName; }
    public void setRuleName(String ruleName) { this.ruleName = ruleName; }

    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }

    public String getAlertStatus() { return alertStatus; }
    public void setAlertStatus(String alertStatus) { this.alertStatus = alertStatus; }

    public String getAlertReason() { return alertReason; }
    public void setAlertReason(String alertReason) { this.alertReason = alertReason; }

    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
