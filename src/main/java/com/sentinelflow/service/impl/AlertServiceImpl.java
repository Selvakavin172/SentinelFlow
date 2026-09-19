package com.sentinelflow.service.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.models.Alert;
import com.sentinelflow.models.Transaction;
import com.sentinelflow.repositories.AlertRepository;
import com.sentinelflow.service.AlertService;

@Service
public class AlertServiceImpl implements AlertService {

    private final AlertRepository alertRepository;

    public AlertServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public void createAlert(
            Transaction transaction,
            RuleResult ruleResult) {

        Alert alert = new Alert();

        alert.setCustomer(
                transaction.getAccount().getCustomer());

        alert.setAccount(
                transaction.getAccount());

        alert.setTransaction(
                transaction);

        alert.setRuleCode(
                ruleResult.getRuleCode());

        alert.setRuleName(
                ruleResult.getRuleCode());

        alert.setRiskScore(
                ruleResult.getRiskScore());

        alert.setAlertStatus("OPEN");

        alert.setAlertReason(
                ruleResult.getExplanation());

        alert.setTriggeredAt(
                LocalDateTime.now());

        alert.setCreatedBy("SYSTEM");

        alertRepository.save(alert);
    }
}