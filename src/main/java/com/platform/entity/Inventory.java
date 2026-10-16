
// This entity represents one product's stock record in the database.
// Product creation uses it to store the initial quantity, and inventory operations update it later.
package com.platform.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Maps the inventory table to a Java class.
// The service and repository use this class to read and save stock information.
@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Each product can have only one inventory record.
    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    // BigDecimal matches the NUMERIC(12,2) database column.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity = BigDecimal.ZERO;

    // New inventory records start with the database's default threshold of 5.
    @Column(name = "low_stock_threshold", nullable = false, precision = 12, scale = 2)
    private BigDecimal lowStockThreshold = BigDecimal.valueOf(5);

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // JPA requires a no-argument constructor.
    public Inventory() {
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getLowStockThreshold() {
        return lowStockThreshold;
    }

    // This value is calculated from the current quantity and threshold, not stored separately.
    public boolean isLowStock() {
        return quantity.compareTo(lowStockThreshold) <= 0;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public void setLowStockThreshold(BigDecimal lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
