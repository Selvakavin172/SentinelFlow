
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
public class RapidMovementRule implements AMLDetectionRule {

    private final RuleConfigRepository ruleConfigRepository;
    private final TransactionRepository transactionRepository;

    public RapidMovementRule(
            RuleConfigRepository ruleConfigRepository,
            TransactionRepository transactionRepository) {

        this.ruleConfigRepository = ruleConfigRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public RuleResult evaluate(Transaction transaction) {

        RuleConfig config =
                ruleConfigRepository.findByRuleCode("RAPID_MOVEMENT")
                        .orElse(null);

        if (config == null || !config.isEnabled()) {
            return RuleResult.notTriggered("RAPID_MOVEMENT");
        }

        if (transaction == null
                || transaction.getAccount() == null
                || transaction.getAccount().getAccountId() == null
                || transaction.getTransactionDatetime() == null) {

            return RuleResult.notTriggered("RAPID_MOVEMENT");
        }

        Long accountId =
                transaction.getAccount().getAccountId();

        LocalDateTime currentTime =
                transaction.getTransactionDatetime();

        int windowHours =
                config.getWindowHours() == null
                        ? 48
                        : config.getWindowHours();

        LocalDateTime start =
                currentTime.minusHours(windowHours);

        LocalDateTime end =
                currentTime;

        List<Transaction> transactions =
                transactionRepository
                        .findTransactionsByAccountAndTimeRange(
                                accountId,
                                start,
                                end);

        BigDecimal depositedAmount =
                BigDecimal.ZERO;

        BigDecimal transferredAmount =
                BigDecimal.ZERO;

        List<Long> evidenceIds =
                new ArrayList<>();

        for (Transaction tx : transactions) {

            if ("CREDIT".equalsIgnoreCase(
                    tx.getTransactionType())) {

                BigDecimal amount =
                        tx.getAmountInr() != null
                                ? tx.getAmountInr()
                                : tx.getAmount();

                if (amount != null
                        && amount.compareTo(BigDecimal.ZERO) > 0) {

                    depositedAmount =
                            depositedAmount.add(amount);

                    if (tx.getTransactionId() != null) {
                        evidenceIds.add(
                                tx.getTransactionId());
                    }
                }
            }
        }

        for (Transaction tx : transactions) {

            if ("DEBIT".equalsIgnoreCase(
                    tx.getTransactionType())) {

                BigDecimal amount =
                        tx.getAmountInr() != null
                                ? tx.getAmountInr()
                                : tx.getAmount();

                if (amount != null
                        && amount.compareTo(BigDecimal.ZERO) > 0) {

                    transferredAmount =
                            transferredAmount.add(amount);

                    if (tx.getTransactionId() != null) {
                        evidenceIds.add(
                                tx.getTransactionId());
                    }
                }
            }
        }

        if ("CREDIT".equalsIgnoreCase(
                transaction.getTransactionType())) {

            BigDecimal currentAmount =
                    transaction.getAmountInr() != null
                            ? transaction.getAmountInr()
                            : transaction.getAmount();

            if (currentAmount != null
                    && currentAmount.compareTo(BigDecimal.ZERO) > 0) {

                depositedAmount =
                        depositedAmount.add(currentAmount);
            }
        }

        if ("DEBIT".equalsIgnoreCase(
                transaction.getTransactionType())) {

            BigDecimal currentAmount =
                    transaction.getAmountInr() != null
                            ? transaction.getAmountInr()
                            : transaction.getAmount();

            if (currentAmount != null
                    && currentAmount.compareTo(BigDecimal.ZERO) > 0) {

                transferredAmount =
                        transferredAmount.add(currentAmount);
            }
        }

        if (depositedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return RuleResult.notTriggered(
                    "RAPID_MOVEMENT");
        }

        BigDecimal percentage =
                transferredAmount
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                depositedAmount,
                                2,
                                java.math.RoundingMode.HALF_UP);

        BigDecimal threshold =
                config.getThresholdValue();

        if (threshold == null
                || threshold.compareTo(BigDecimal.ZERO) <= 0) {

            return RuleResult.notTriggered(
                    "RAPID_MOVEMENT");
        }

        if (percentage.compareTo(threshold) >= 0) {

            if (transaction.getTransactionId() != null) {
                evidenceIds.add(
                        transaction.getTransactionId());
            }

            return new RuleResult(
                    true,
                    "RAPID_MOVEMENT",
                    "At least "
                            + threshold
                            + "% of deposited funds were transferred out within "
                            + windowHours
                            + " hours",
                    config.getRiskScore() == null
                            ? 0
                            : config.getRiskScore(),
                    evidenceIds
            );
        }

        return RuleResult.notTriggered(
                "RAPID_MOVEMENT");
    }
}
