package com.sentinelflow.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sentinelflow.dto.AlertResponse;
import com.sentinelflow.models.Alert;
import com.sentinelflow.repositories.AlertRepository;

@RestController
@RequestMapping("/api/v1/alerts")
public class AlertController {

    private final AlertRepository alertRepository;

    public AlertController(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @GetMapping
    public List<AlertResponse> getAllAlerts() {
        return alertRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/open")
    public List<AlertResponse> getOpenAlerts() {
        return alertRepository.findOpenAlertsSortedByRiskScore()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertResponse> getAlertById(@PathVariable("id") Long id) {
        return alertRepository.findById(id)
                .map(a -> ResponseEntity.ok(toResponse(a)))
                .orElse(ResponseEntity.notFound().build());
    }

    private AlertResponse toResponse(Alert alert) {
        AlertResponse r = new AlertResponse();
        r.setAlertId(alert.getAlertId());
        if (alert.getAccount() != null) r.setAccountId(alert.getAccount().getAccountId());
        if (alert.getTransaction() != null) r.setTransactionId(alert.getTransaction().getTransactionId());
        r.setRuleCode(alert.getRuleCode());
        r.setRuleName(alert.getRuleName());
        r.setRiskScore(alert.getRiskScore());
        r.setAlertStatus(alert.getAlertStatus());
        r.setAlertReason(alert.getAlertReason());
        r.setTriggeredAt(alert.getTriggeredAt());
        r.setCreatedBy(alert.getCreatedBy());
        return r;
    }
}
