// This controller handles HTTP requests for product inventory.
// It passes requests to InventoryService, which applies stock rules and saves changes.
package com.platform.controller;

import com.platform.entity.Inventory;
import com.platform.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

// Handles inventory API requests and delegates inventory work to InventoryService.
@RestController
@RequestMapping("/api/businesses/{businessId}/products/{productId}/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    // Spring provides the service through constructor-based dependency injection.
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Gets a product's inventory for the specified business.
     *
     * @param businessId business ID from the URL
     * @param productId product ID from the URL
     * @return the inventory record
     */
    @GetMapping
    public Inventory getInventory(
            @PathVariable Long businessId,
            @PathVariable Long productId) {
        return inventoryService.getInventory(businessId, productId);
    }

    /**
     * Sets a product's stock to the quantity in the request.
     *
     * @param businessId business ID from the URL
     * @param productId product ID from the URL
     * @param request JSON body containing a non-negative quantity
     * @return the updated inventory record
     */
    @PutMapping
    public Inventory updateStock(
            @PathVariable Long businessId,
            @PathVariable Long productId,
            @Valid @RequestBody QuantityRequest request) {
        return inventoryService.updateStock(
                businessId, productId, request.getQuantity());
    }

    /**
     * Adds a positive quantity to a product's current stock.
     *
     * @param businessId business ID from the URL
     * @param productId product ID from the URL
     * @param request JSON body containing the quantity to add
     * @return the updated inventory record
     */
    @PostMapping("/add")
    public Inventory addStock(
            @PathVariable Long businessId,
            @PathVariable Long productId,
            @Valid @RequestBody QuantityRequest request) {
        // InventoryService also rejects zero for an addition and returns HTTP 400.
        return inventoryService.addStock(
                businessId, productId, request.getQuantity());
    }

    // Keeps the request limited to the stock quantity instead of accepting the whole database entity.
    public static class QuantityRequest {

        @NotNull
        @DecimalMin(value = "0.0", message = "Quantity cannot be negative")
        private BigDecimal quantity;

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }
    }
}
