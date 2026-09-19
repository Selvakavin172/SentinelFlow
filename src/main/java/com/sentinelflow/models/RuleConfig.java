package com.sentinelflow.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "rule_config")
public class RuleConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_code", nullable = false, unique = true)
    private String ruleCode;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    private boolean enabled;

    @Column(name = "threshold_value")
    private BigDecimal thresholdValue;

    @Column(name = "secondary_threshold_value")
    private BigDecimal secondaryThresholdValue;

    @Column(name = "window_hours")
    private Integer windowHours;

    @Column(name = "risk_score")
    private Integer riskScore;

    private String description;


    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public BigDecimal getThresholdValue() {
        return thresholdValue;
    }

    public void setThresholdValue(BigDecimal thresholdValue) {
        this.thresholdValue = thresholdValue;
    }

    public BigDecimal getSecondaryThresholdValue() {
        return secondaryThresholdValue;
    }

    public void setSecondaryThresholdValue(BigDecimal secondaryThresholdValue) {
        this.secondaryThresholdValue = secondaryThresholdValue;
    }

    public Integer getWindowHours() {
        return windowHours;
    }

    public void setWindowHours(Integer windowHours) {
        this.windowHours = windowHours;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}