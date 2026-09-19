
package com.sentinelflow.aml.rule;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        // Load rule configuration
        RuleConfig config = ruleConfigRepository
                .findByRuleCode("RAPID_MOVEMENT")
                .orElse(null);

        if (config == null || !config.isEnabled()) {
            return RuleResult.notTriggered("RAPID_MOVEMENT");
        }

        // Validate transaction
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
                config.getWindowHours() != null
                        ? config.getWindowHours()
                        : 48;

        LocalDateTime start =
                currentTime.minusHours(windowHours);

        // Get only PREVIOUS transactions.
        // Current transaction is not saved yet.
        List<Transaction> previousTransactions =
                transactionRepository
                        .findTransactionsByAccountAndTimeRange(
                                accountId,
                                start,
                                currentTime);

        BigDecimal depositedAmount =
                BigDecimal.ZERO;

        BigDecimal transferredAmount =
                BigDecimal.ZERO;

        List<Long> evidenceIds =
                new ArrayList<>();

        // ------------------------------------------------
        // Previous transactions
        // ------------------------------------------------

        for (Transaction tx : previousTransactions) {

            if (tx.getAmount() == null) {
                continue;
            }

            BigDecimal amount =
                    tx.getAmountInr() != null
                            ? tx.getAmountInr()
                            : tx.getAmount();

            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            if ("CREDIT".equalsIgnoreCase(
                    tx.getTransactionType())) {

                depositedAmount =
                        depositedAmount.add(amount);

                if (tx.getTransactionId() != null) {
                    evidenceIds.add(
                            tx.getTransactionId());
                }
            }

            else if ("DEBIT".equalsIgnoreCase(
                    tx.getTransactionType())) {

                transferredAmount =
                        transferredAmount.add(amount);

                if (tx.getTransactionId() != null) {
                    evidenceIds.add(
                            tx.getTransactionId());
                }
            }
        }

        // ------------------------------------------------
        // Current transaction
        // ------------------------------------------------

        BigDecimal currentAmount =
                transaction.getAmountInr() != null
                        ? transaction.getAmountInr()
                        : transaction.getAmount();

        if (currentAmount == null
                || currentAmount.compareTo(BigDecimal.ZERO) <= 0) {

            return RuleResult.notTriggered(
                    "RAPID_MOVEMENT");
        }

        if ("CREDIT".equalsIgnoreCase(
                transaction.getTransactionType())) {

            depositedAmount =
                    depositedAmount.add(currentAmount);
        }

        else if ("DEBIT".equalsIgnoreCase(
                transaction.getTransactionType())) {

            transferredAmount =
                    transferredAmount.add(currentAmount);
        }

        else {
            return RuleResult.notTriggered(
                    "RAPID_MOVEMENT");
        }

        // ------------------------------------------------
        // No deposits = no rapid movement
        // ------------------------------------------------

        if (depositedAmount.compareTo(
                BigDecimal.ZERO) <= 0) {

            return RuleResult.notTriggered(
                    "RAPID_MOVEMENT");
        }

        // ------------------------------------------------
        // Calculate percentage
        // ------------------------------------------------

        BigDecimal percentage =
                transferredAmount
                        .multiply(BigDecimal.valueOf(100))
                        .divide(
                                depositedAmount,
                                2,
                                RoundingMode.HALF_UP);

        BigDecimal threshold =
                config.getThresholdValue();

        if (threshold == null
                || threshold.compareTo(BigDecimal.ZERO) <= 0) {

            return RuleResult.notTriggered(
                    "RAPID_MOVEMENT");
        }

        // ------------------------------------------------
        // Trigger Rapid Movement
        // ------------------------------------------------

        if (percentage.compareTo(threshold) >= 0) {

            return new RuleResult(
                    true,
                    "RAPID_MOVEMENT",
                    "Transferred "
                            + percentage
                            + "% of deposited funds within "
                            + windowHours
                            + " hours. Threshold: "
                            + threshold
                            + "%",
                    config.getRiskScore() != null
                            ? config.getRiskScore()
                            : 0,
                    evidenceIds
            );
        }

        return RuleResult.notTriggered(
                "RAPID_MOVEMENT");
    }
}
