package com.sentinelflow.aml.rule;

import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.models.Transaction;

public interface AMLDetectionRule {

    RuleResult evaluate(Transaction transaction);
}