package com.sentinelflow.aml.rule;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.models.RuleConfig;
import com.sentinelflow.models.Transaction;
import com.sentinelflow.repositories.HighRiskJurisdictionRepository;
import com.sentinelflow.repositories.RuleConfigRepository;

@Component
public class HighRiskJurisdictionRule implements AMLDetectionRule {

    private final RuleConfigRepository ruleConfigRepository;
    private final HighRiskJurisdictionRepository jurisdictionRepository;

    public HighRiskJurisdictionRule(
            RuleConfigRepository ruleConfigRepository,
            HighRiskJurisdictionRepository jurisdictionRepository) {

        this.ruleConfigRepository = ruleConfigRepository;
        this.jurisdictionRepository = jurisdictionRepository;
    }

    @Override
    public RuleResult evaluate(Transaction transaction) {

        RuleConfig config = ruleConfigRepository
                .findByRuleCode("HIGH_RISK_JURISDICTION")
                .orElse(null);

        if (config == null || !config.isEnabled()) {
            return RuleResult.notTriggered("HIGH_RISK_JURISDICTION");
        }

        // Use the transaction's countryCode field (normalized) for jurisdiction checks
        String countryCode = transaction.getCountryCode();
        if (countryCode == null || countryCode.isBlank()) {
            return RuleResult.notTriggered("HIGH_RISK_JURISDICTION");
        }

        boolean highRisk = jurisdictionRepository.existsByCountryCodeAndActive(countryCode, true);

        if (highRisk) {
            return new RuleResult(
                    true,
                    config.getRuleCode(),
                    "Transaction involves a configured high-risk jurisdiction",
                    config.getRiskScore() == null ? 0 : config.getRiskScore(),
                    List.of()
            );
        }

        return RuleResult.notTriggered("HIGH_RISK_JURISDICTION");
    }
}