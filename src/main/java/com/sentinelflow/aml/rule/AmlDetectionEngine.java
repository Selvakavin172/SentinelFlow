package com.sentinelflow.aml.rule;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.models.Transaction;

@Service
public class AmlDetectionEngine {

    private final List<AMLDetectionRule> rules;

    public AmlDetectionEngine(List<AMLDetectionRule> rules) {
        this.rules = rules;
    }

    public List<RuleResult> evaluate(Transaction transaction) {

        return rules.stream()
                .map(rule -> rule.evaluate(transaction))
                .filter(RuleResult::isTriggered)
                .toList();
    }
}