package com.sentinelflow.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sentinelflow.models.HighRiskJurisdiction;

public interface HighRiskJurisdictionRepository
        extends JpaRepository<HighRiskJurisdiction, Long> {

    boolean existsByCountryCodeAndActive(
            String countryCode,
            boolean active);
}