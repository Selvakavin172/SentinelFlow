package com.sentinelflow.service;

import com.sentinelflow.dto.RuleResult;
import com.sentinelflow.models.Transaction;

public interface AlertService {

    void createAlert(Transaction transaction, RuleResult ruleResult);

}