
// This service contains the rules for reading and changing product stock.
// It checks product ownership and uses InventoryRepository to save inventory changes.
package com.platform.service;

import com.platform.entity.Inventory;
import com.platform.repository.InventoryRepository;
import com.platform.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

// This class keeps stock rules together and uses the repositories to read and save inventory.
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    /**
     * Receives the repositories needed to check product ownership and save inventory.
     *
     * @param inventoryRepository database operations for inventory records
     * @param productRepository database operations for checking product ownership
     */
    public InventoryService(
            InventoryRepository inventoryRepository,
            ProductRepository productRepository) {

        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    /**
     * Gets the inventory record when a business requests a product's stock.
     *
     * @param businessId ID of the business that must own the product
     * @param productId ID of the product whose inventory is requested
     * @return the product's inventory record
     */
    @Transactional
    public Inventory getInventory(Long businessId, Long productId) {
        return getOrCreateInventory(businessId, productId);
    }

    /**
     * Adds a positive amount to the product's current stock.
     *
     * @param businessId ID of the business that must own the product
     * @param productId ID of the product whose stock will change
     * @param quantity positive amount to add to the current stock
     * @return the saved inventory record with the new quantity
     */
    @Transactional
    public Inventory addStock(
            Long businessId,
            Long productId,
            BigDecimal quantity) {

        // Stock additions must be greater than zero to avoid invalid updates.
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    BAD_REQUEST, "Quantity must be greater than zero");
        }

        Inventory inventory = getOrCreateInventory(businessId, productId);

        // Calculate the new stock value, then record when this update happened.
        BigDecimal updatedQuantity = inventory.getQuantity().add(quantity);

        inventory.setQuantity(updatedQuantity);
        inventory.setUpdatedAt(LocalDateTime.now());

        return inventoryRepository.save(inventory);
    }

    /**
     * Replaces the current stock with a non-negative quantity.
     *
     * @param businessId ID of the business that must own the product
     * @param productId ID of the product whose stock will change
     * @param quantity new stock amount
     * @return the saved inventory record with the supplied quantity
     */
    @Transactional
    public Inventory updateStock(
            Long businessId,
            Long productId,
            BigDecimal quantity) {

        // Reject negative stock before loading or changing the inventory record.
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(
                    BAD_REQUEST, "Quantity cannot be negative");
        }

        Inventory inventory = getOrCreateInventory(businessId, productId);

        // Save the supplied quantity and refresh the last-updated time.
        inventory.setQuantity(quantity);
        inventory.setUpdatedAt(LocalDateTime.now());

        return inventoryRepository.save(inventory);
    }

    // First confirms the product belongs to this business, then returns or creates its inventory.
    private Inventory getOrCreateInventory(
            Long businessId,
            Long productId) {

        // Match both IDs so a business cannot access another business's product stock.
        boolean productExists = productRepository
                .findByIdAndBusinessId(productId, businessId)
                .isPresent();

        if (!productExists) {
            throw new ResponseStatusException(
                    NOT_FOUND, "Product not found");
        }

        return inventoryRepository.findByProductId(productId)
                .orElseGet(() -> createInventory(productId));
    }

    // Creates an inventory record with zero stock.
    private Inventory createInventory(Long productId) {
        Inventory inventory = new Inventory();

        inventory.setProductId(productId);
        inventory.setQuantity(BigDecimal.ZERO);
        inventory.setUpdatedAt(LocalDateTime.now());

        return inventoryRepository.save(inventory);
    }
}
