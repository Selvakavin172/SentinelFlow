package com.sentinelflow.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "case_table", indexes = {
    @Index(name = "idx_case_customer", columnList = "customer_id"),
    @Index(name = "idx_case_status", columnList = "case_status"),
    @Index(name = "idx_case_severity", columnList = "severity"),
    @Index(name = "idx_case_assigned", columnList = "assigned_to")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AmlCase {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "case_seq")
    @SequenceGenerator(name = "case_seq", sequenceName = "case_id_seq", allocationSize = 1)
    @Column(name = "case_id")
    private Long caseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "case_title", length = 200)
    private String caseTitle;

    @Column(name = "case_status", length = 30)
    private String caseStatus;

    @Column(name = "severity", length = 20)
    private String severity;

    @Column(name = "assigned_to", length = 100)
    private String assignedTo;

    @Column(name = "opened_at")
    private LocalDateTime openedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "disposition_reason", length = 200)
    private String dispositionReason;

    @Column(name = "investigation_notes", columnDefinition = "TEXT")
    private String investigationNotes;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        openedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (caseStatus == null) {
            caseStatus = "OPEN";
        }
        if (severity == null) {
            severity = "MEDIUM";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
