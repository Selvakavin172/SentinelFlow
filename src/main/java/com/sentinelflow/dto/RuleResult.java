package com.sentinelflow.dto;

import java.util.Collections;
import java.util.List;

public class RuleResult {

    private boolean triggered;
    private String ruleCode;
    private String explanation;
    private int riskScore;
    private List<Long> evidenceTransactionIds;

    public RuleResult(boolean triggered, String ruleCode, String explanation, int riskScore) {
        this(triggered, ruleCode, explanation, riskScore, Collections.emptyList());
    }

    public RuleResult(boolean triggered, String ruleCode, String explanation, int riskScore, List<Long> evidenceTransactionIds) {
        this.triggered = triggered;
        this.ruleCode = ruleCode;
        this.explanation = explanation;
        this.riskScore = riskScore;
        this.evidenceTransactionIds = evidenceTransactionIds == null ? Collections.emptyList() : evidenceTransactionIds;
    }

    public static RuleResult notTriggered(String ruleCode) {
        return new RuleResult(false, ruleCode, "Not triggered", 0, Collections.emptyList());
    }

    public boolean isTriggered() {
        return triggered;
    }

    public String getRuleCode() {
        return ruleCode;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public List<Long> getEvidenceTransactionIds() {
        return evidenceTransactionIds;
    }
}