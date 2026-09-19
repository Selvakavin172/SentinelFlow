
package com.sentinelflow.aml.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.models.RuleConfig;
import com.sentinelflow.models.Transaction;
import com.sentinelflow.repositories.RuleConfigRepository;
import com.sentinelflow.repositories.TransactionRepository;

@Component
public class BehavioralDedectionRule implements AMLDetectionRule {

    private final RuleConfigRepository ruleConfigRepository;
    private final TransactionRepository transactionRepository;

    public BehavioralDedectionRule(
            RuleConfigRepository ruleConfigRepository,
            TransactionRepository transactionRepository) {

        this.ruleConfigRepository = ruleConfigRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public RuleResult evaluate(Transaction transaction) {

        RuleConfig config = ruleConfigRepository
                .findByRuleCode("BEHAVIORAL_DETECTION")
                .orElse(null);

        if (config == null || !config.isEnabled()) {
            return RuleResult.notTriggered("BEHAVIORAL_DETECTION");
        }

        if (transaction == null
                || transaction.getAccount() == null
                || transaction.getAccount().getAccountId() == null
                || transaction.getTransactionDatetime() == null) {

            return RuleResult.notTriggered("BEHAVIORAL_DETECTION");
        }

        Long accountId =
                transaction.getAccount().getAccountId();

        LocalDateTime currentTime =
                transaction.getTransactionDatetime();

        LocalDateTime dailyStart =
                currentTime.minusDays(1);

        BigDecimal dailyTotal =
                transactionRepository.calculateAccountTransactionTotal(
                        accountId,
                        dailyStart,
                        currentTime
                );

        if (dailyTotal == null) {
            dailyTotal = BigDecimal.ZERO;
        }

        BigDecimal currentAmount =
                transaction.getAmountInr() != null
                        ? transaction.getAmountInr()
                        : transaction.getAmount();

        if (currentAmount != null
                && currentAmount.compareTo(BigDecimal.ZERO) > 0) {

            dailyTotal = dailyTotal.add(currentAmount);
        }

        LocalDateTime baselineEnd =
                dailyStart;

        LocalDateTime baselineStart =
                baselineEnd.minusDays(90);

        BigDecimal ninetyDayTotal =
                transactionRepository
                        .calculateAccountTransactionTotalFor90Days(
                                accountId,
                                baselineStart,
                                baselineEnd
                        );

        if (ninetyDayTotal == null
                || ninetyDayTotal.compareTo(BigDecimal.ZERO) <= 0) {

            return RuleResult.notTriggered(
                    "BEHAVIORAL_DETECTION");
        }

        BigDecimal ninetyDayAverage =
                ninetyDayTotal.divide(
                        BigDecimal.valueOf(90),
                        2,
                        RoundingMode.HALF_UP
                );

        BigDecimal thresholdMultiplier =
                config.getThresholdValue();

        if (thresholdMultiplier == null
                || thresholdMultiplier.compareTo(BigDecimal.ZERO) <= 0) {

            return RuleResult.notTriggered(
                    "BEHAVIORAL_DETECTION");
        }

        BigDecimal allowedAmount =
                ninetyDayAverage.multiply(
                        thresholdMultiplier
                );

        if (dailyTotal.compareTo(allowedAmount) > 0) {

            int riskScore =
                    config.getRiskScore() != null
                            ? config.getRiskScore()
                            : 0;

            String explanation =
                    "Daily transaction value of "
                            + dailyTotal
                            + " exceeds "
                            + thresholdMultiplier
                            + "x the 90-day average of "
                            + ninetyDayAverage;

            return new RuleResult(
                    true,
                    "BEHAVIORAL_DETECTION",
                    explanation,
                    riskScore,
                    List.of()
            );
        }

        return RuleResult.notTriggered(
                "BEHAVIORAL_DETECTION");
    }
}
