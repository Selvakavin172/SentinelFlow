package com.sentinelflow.aml.rule;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.models.RuleConfig;
import com.sentinelflow.models.Transaction;
import com.sentinelflow.repositories.RuleConfigRepository;

@Component
public class ThresholdRule implements AMLDetectionRule {

    private final RuleConfigRepository ruleConfigRepository;

    public ThresholdRule(RuleConfigRepository ruleConfigRepository) {
        this.ruleConfigRepository = ruleConfigRepository;
    }

    @Override
    public RuleResult evaluate(Transaction transaction) {

        RuleConfig config =
                ruleConfigRepository.findByRuleCode("THRESHOLD")
                        .orElse(null);

        if (config == null || !config.isEnabled()) {
            return RuleResult.notTriggered("THRESHOLD");
        }

        BigDecimal amount = transaction.getAmountInr() != null ? transaction.getAmountInr() : transaction.getAmount();

        if (amount == null) {
            return RuleResult.notTriggered("THRESHOLD");
        }

        if (amount.compareTo(config.getThresholdValue()) >= 0) {

            return new RuleResult(
                    true,
                    config.getRuleCode(),
                    "Transaction amount exceeds configured threshold",
                    config.getRiskScore() == null ? 0 : config.getRiskScore(),
                    List.of()
            );
        }

        return RuleResult.notTriggered("THRESHOLD");
    }
}