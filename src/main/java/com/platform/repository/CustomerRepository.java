// This repository provides database access for customer records.
// Its methods keep customer lookups scoped to a business.
package com.platform.repository;

import com.platform.entity.Customer;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// JpaRepository supplies standard database operations; Spring Data creates this implementation.
// Each custom method includes businessId to keep customer searches business-scoped.
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Returns only the customers owned by the requested business.
    List<Customer> findAllByBusinessIdOrderByIdAsc(Long businessId);

    // Finds a customer only when both the customer ID and business ID match.
    Optional<Customer> findByIdAndBusinessId(Long customerId, Long businessId);

    // Locks the matching customer row while a service creates its open conversation.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select customer from Customer customer "
            + "where customer.id = :customerId and customer.businessId = :businessId")
    Optional<Customer> findByIdAndBusinessIdForUpdate(
            @Param("customerId") Long customerId,
            @Param("businessId") Long businessId);

    // Finds a customer's WhatsApp phone number only within the requested business.
    Optional<Customer> findByBusinessIdAndPhone(Long businessId, String phone);

    // Checks for a duplicate phone number before creating a customer.
    boolean existsByBusinessIdAndPhone(Long businessId, String phone);

    // Checks for a duplicate phone number during an update, excluding that customer.
    boolean existsByBusinessIdAndPhoneAndIdNot(
            Long businessId, String phone, Long customerId);
}
