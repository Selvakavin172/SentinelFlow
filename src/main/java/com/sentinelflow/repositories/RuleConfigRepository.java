package com.sentinelflow.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sentinelflow.models.RuleConfig;

public interface RuleConfigRepository
        extends JpaRepository<RuleConfig, Long> {

    Optional<RuleConfig> findByRuleCode(String ruleCode);
}