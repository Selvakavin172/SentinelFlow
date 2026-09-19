package com.sentinelflow.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "customer", indexes = {
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_risk_rating", columnList = "risk_rating"),
    @Index(name = "idx_kyc_status", columnList = "customer_kyc_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "customer_id_seq", allocationSize = 1)
    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "gender", length = 20)
    private String gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "age")
    private Integer age;

    @Column(name = "email", unique = true, length = 150)
    private String email;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "occupation", length = 100)
    private String occupation;

    @Column(name = "annual_income", precision = 18, scale = 2)
    private BigDecimal annualIncome;

    @Column(name = "marital_status", length = 50)
    private String maritalStatus;

    @Column(name = "education", length = 100)
    private String education;

    @Column(name = "employment_type", length = 50)
    private String employmentType;

    @Column(name = "customer_kyc_status", length = 50)
    private String kycStatus;

    @Column(name = "risk_rating", length = 20)
    private String riskRating;

    @Column(name = "is_politically_exposed")
    private Boolean politicallyExposed;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "phone_verified")
    private Boolean phoneVerified;

    @Column(name = "num_complaints")
    private Integer numComplaints;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Account> accounts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Alert> alerts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<AmlCase> cases;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
        if (politicallyExposed == null) politicallyExposed = false;
        if (emailVerified == null) emailVerified = false;
        if (phoneVerified == null) phoneVerified = false;
        if (numComplaints == null) numComplaints = 0;
        if (riskRating == null) riskRating = "LOW";
        if (kycStatus == null) kycStatus = "PENDING";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
