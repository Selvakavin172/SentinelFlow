package com.sentinelflow.repositories;

import com.sentinelflow.models.AmlCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AmlCaseRepository extends JpaRepository<AmlCase, Long> {

    @Query("SELECT c FROM AmlCase c WHERE c.customer.customerId = :customerId")
    List<AmlCase> findByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT c FROM AmlCase c WHERE c.caseStatus = :status")
    List<AmlCase> findByCaseStatus(@Param("status") String caseStatus);

    @Query("SELECT c FROM AmlCase c WHERE c.severity = :severity")
    List<AmlCase> findBySeverity(@Param("severity") String severity);

    @Query("SELECT c FROM AmlCase c WHERE c.assignedTo = :assignedTo")
    List<AmlCase> findByAssignedTo(@Param("assignedTo") String assignedTo);

    @Query("SELECT c FROM AmlCase c WHERE c.caseStatus = 'OPEN' ORDER BY c.severity DESC, c.openedAt DESC")
    List<AmlCase> findOpenCasesSortedBySeverity();

    @Query("SELECT c FROM AmlCase c WHERE c.customer.customerId = :customerId AND c.caseStatus = 'OPEN'")
    List<AmlCase> findOpenCasesByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT c FROM AmlCase c WHERE c.caseStatus = 'OPEN' AND c.assignedTo = :analyst ORDER BY c.openedAt DESC")
    List<AmlCase> findOpenCasesAssignedToAnalyst(@Param("analyst") String analyst);

    @Query("SELECT c FROM AmlCase c WHERE c.severity IN ('HIGH', 'CRITICAL') AND c.caseStatus = 'OPEN' ORDER BY c.openedAt ASC")
    List<AmlCase> findHighSeverityCases();

    @Query("SELECT COUNT(c) FROM AmlCase c WHERE c.caseStatus = 'OPEN'")
    Long countOpenCases();

    @Query("SELECT COUNT(c) FROM AmlCase c WHERE c.customer.customerId = :customerId AND c.caseStatus = 'OPEN'")
    Long countOpenCasesByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT c FROM AmlCase c WHERE c.openedAt BETWEEN :startDate AND :endDate ORDER BY c.openedAt DESC")
    List<AmlCase> findByOpenedAtBetweenOrderByOpenedAtDesc(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT c FROM AmlCase c WHERE c.caseStatus = 'CLOSED' AND c.closedAt BETWEEN :startTime AND :endTime ORDER BY c.closedAt DESC")
    List<AmlCase> findClosedCasesByDateRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT c FROM AmlCase c WHERE c.caseStatus = 'OPEN' AND c.assignedTo IS NULL")
    List<AmlCase> findUnassignedOpenCases();

    @Query("SELECT COUNT(c) FROM AmlCase c WHERE c.assignedTo = :analyst AND c.caseStatus = 'OPEN'")
    Long countOpenCasesAssignedToAnalyst(@Param("analyst") String analyst);
}

