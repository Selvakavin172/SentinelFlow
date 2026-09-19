
package com.sentinelflow.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "high_risk_jurisdiction")
public class HighRiskJurisdiction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country_code", nullable = false, unique = true)
    private String countryCode;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "risk_level")
    private String riskLevel;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}