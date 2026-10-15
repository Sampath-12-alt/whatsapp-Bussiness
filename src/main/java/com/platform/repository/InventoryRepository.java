
// This repository provides database access for inventory records.
// It is needed so services can save stock and find it by product ID.
package com.platform.repository;

import com.platform.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// JpaRepository provides standard database operations; Spring Data supplies the implementation.
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * Finds the inventory record for a product.
     *
     * @param productId ID of the product whose inventory is requested
     * @return an Optional containing the inventory record, or empty when none exists
     */
    Optional<Inventory> findByProductId(Long productId);
}
