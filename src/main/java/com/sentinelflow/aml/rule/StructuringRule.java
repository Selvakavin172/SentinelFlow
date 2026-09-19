
package com.sentinelflow.aml.rule;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.models.RuleConfig;
import com.sentinelflow.models.Transaction;
import com.sentinelflow.repositories.RuleConfigRepository;
import com.sentinelflow.repositories.TransactionRepository;

@Component
public class StructuringRule implements AMLDetectionRule {

    private final RuleConfigRepository ruleConfigRepository;
    private final TransactionRepository transactionRepository;

    public StructuringRule(
            RuleConfigRepository ruleConfigRepository,
            TransactionRepository transactionRepository) {

        this.ruleConfigRepository = ruleConfigRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public RuleResult evaluate(Transaction transaction) {

        RuleConfig config =
                ruleConfigRepository.findByRuleCode("STRUCTURING")
                        .orElse(null);

        if (config == null || !config.isEnabled()) {
            return RuleResult.notTriggered("STRUCTURING");
        }

        if (transaction == null
                || transaction.getAccount() == null
                || transaction.getAccount().getAccountId() == null) {

            return RuleResult.notTriggered("STRUCTURING");
        }

        Long accountId =
                transaction.getAccount().getAccountId();

        LocalDateTime end =
                transaction.getTransactionDatetime();

        if (end == null) {
            return RuleResult.notTriggered("STRUCTURING");
        }

        int windowHours =
                config.getWindowHours() == null
                        ? 24
                        : config.getWindowHours();

        LocalDateTime start =
                end.minusHours(windowHours);

        List<Transaction> transactions =
                transactionRepository
                        .findTransactionsByAccountAndTimeRange(
                                accountId,
                                start,
                                end);

        BigDecimal lowerLimit =
                config.getThresholdValue();

        BigDecimal upperLimit =
                config.getSecondaryThresholdValue();

        if (lowerLimit == null || upperLimit == null) {
            return RuleResult.notTriggered("STRUCTURING");
        }

        List<Long> evidenceIds =
                new ArrayList<>();

        // Check already saved transactions
        for (Transaction tx : transactions) {

            BigDecimal txAmount =
                    tx.getAmountInr() != null
                            ? tx.getAmountInr()
                            : tx.getAmount();

            if (txAmount != null
                    && txAmount.compareTo(lowerLimit) >= 0
                    && txAmount.compareTo(upperLimit) <= 0) {

                if (tx.getTransactionId() != null) {
                    evidenceIds.add(tx.getTransactionId());
                }
            }
        }

        // Add current transaction because it is not saved yet
        BigDecimal currentAmount =
                transaction.getAmountInr() != null
                        ? transaction.getAmountInr()
                        : transaction.getAmount();

        if (currentAmount != null
                && currentAmount.compareTo(lowerLimit) >= 0
                && currentAmount.compareTo(upperLimit) <= 0) {

            // Current transaction has no ID yet.
            // We only need it for the count.
            evidenceIds.add(null);
        }

        if (evidenceIds.size() >= 3) {

            // Remove null before storing evidence
            evidenceIds.removeIf(id -> id == null);

            return new RuleResult(
                    true,
                    config.getRuleCode(),
                    "Three or more near-threshold transactions detected within "
                            + windowHours + " hours",
                    config.getRiskScore() == null
                            ? 0
                            : config.getRiskScore(),
                    evidenceIds
            );
        }

        return RuleResult.notTriggered("STRUCTURING");
    }
}
