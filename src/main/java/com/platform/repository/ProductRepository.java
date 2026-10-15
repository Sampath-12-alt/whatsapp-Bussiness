
// Defines database operations for Product; Spring Data JPA creates the implementation at runtime.
package com.platform.repository;

import com.platform.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Provides standard database operations and product lookups.
 * JpaRepository<Product, Long> supplies common methods such as save, findById, and delete;
 * Spring Data derives the two custom query implementations from their method names.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds all products owned by a business, keeping this lookup scoped to that business.
     *
     * @param businessId ID of the business whose products are requested
     * @return matching products, or an empty list when there are none
     */
    List<Product> findByBusinessId(Long businessId);

    /**
     * Finds a product only when both its ID and owning business ID match.
     *
     * @param productId ID of the product to find
     * @param businessId ID of the business that must own the product
     * @return an Optional containing the product when found, otherwise an empty Optional
     */
    Optional<Product> findByIdAndBusinessId(
            Long productId, Long businessId);
}
