package com.sentinelflow.repositories;

import com.sentinelflow.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);

    @Query("SELECT c FROM Customer c WHERE c.phone = :phone")
    Optional<Customer> findByPhone(@Param("phone") String phone);

    @Query("SELECT c FROM Customer c WHERE c.riskRating = :riskRating")
    List<Customer> findByRiskRating(@Param("riskRating") String riskRating);

    @Query("SELECT c FROM Customer c WHERE c.kycStatus = :kycStatus")
    List<Customer> findByKycStatus(@Param("kycStatus") String kycStatus);

    @Query("SELECT c FROM Customer c WHERE c.politicallyExposed = true")
    List<Customer> findByPoliticallyExposedTrue();

    @Query("SELECT c FROM Customer c WHERE c.isActive = true AND c.numComplaints > :complaints")
    List<Customer> findByIsActiveTrueAndNumComplaintsGreaterThan(@Param("complaints") Integer complaints);

    @Query("SELECT c FROM Customer c WHERE c.country = :country AND c.isActive = true")
    List<Customer> findActiveCustomersByCountry(@Param("country") String country);

    @Query("SELECT c FROM Customer c WHERE c.riskRating IN ('HIGH', 'VERY_HIGH')")
    List<Customer> findHighRiskCustomers();

    @Query("SELECT c FROM Customer c WHERE c.isActive = true AND c.emailVerified = true AND c.phoneVerified = true")
    List<Customer> findVerifiedCustomers();

    @Query("SELECT c FROM Customer c WHERE c.politicallyExposed = true AND c.riskRating IN ('HIGH', 'VERY_HIGH')")
    List<Customer> findExposedHighRiskCustomers();
}
