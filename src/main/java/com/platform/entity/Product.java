
// Maps a product row in the products table to a Java object used by the API and service.
package com.platform.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// @Entity tells JPA this class is stored as a database entity.
// @Table selects the database table name instead of relying on a default.
@Entity
@Table(name = "products")
public class Product {

    // @Id marks the primary key; @GeneratedValue lets the database assign its value.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // A product belongs to one business; this ID is also used to keep product queries scoped.
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    // Optional category reference; this field maps to category_id in the products table.
    @Column(name = "category_id")
    private Long categoryId;

    // @Column customizes the database column; nullable=false means a name is required.
    @Column(nullable = false)
    private String name;

    private String description;

    // BigDecimal avoids floating-point rounding issues for money; precision and scale define the stored decimal size.
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    // The unit describes how this product is measured or sold, such as "piece" or "kg".
    @Column(nullable = false, length = 50)
    private String unit;

    // Products are available by default unless explicitly marked otherwise.
    @Column(nullable = false)
    private Boolean available = true;

    // Creation time cannot be updated after insertion; updatedAt is refreshed when the service changes the product.
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // JPA needs a no-argument constructor to create Product objects when loading rows from the database.
    public Product() {
    }

    // Getters and setters let Spring/JPA and other Java code read or update these private fields.

    /** Returns the database ID assigned to this product. */
    public Long getId() {
        return id;
    }

    /** Returns the ID of the business that owns this product. */
    public Long getBusinessId() {
        return businessId;
    }

    /** Returns this product's optional category ID. */
    public Long getCategoryId() {
        return categoryId;
    }

    /** Returns the product name. */
    public String getName() {
        return name;
    }

    /** Returns the product description, or null when none was supplied. */
    public String getDescription() {
        return description;
    }

    /** Returns the product price as a decimal amount. */
    public BigDecimal getPrice() {
        return price;
    }

    /** Returns the unit used to describe or sell the product. */
    public String getUnit() {
        return unit;
    }

    /** Returns whether the product is currently available. */
    public Boolean getAvailable() {
        return available;
    }

    /** Returns when the product row was created. */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the database ID.
     *
     * @param id the product ID; the service clears it before creating a new product
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Sets the owning business.
     *
     * @param businessId the ID of the business that owns this product
     */
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    /**
     * Sets the optional category.
     *
     * @param categoryId the ID of the category, or null when there is no category
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * Sets the product name.
     *
     * @param name the name to store for this product
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the product description.
     *
     * @param description descriptive text, or null when no description is provided
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the product price.
     *
     * @param price the decimal amount charged for the product
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Sets the product's unit.
     *
     * @param unit the measurement or selling unit, such as "piece"
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Sets the availability flag.
     *
     * @param available true when customers can order the product; otherwise false
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }

    /**
     * Sets when the product was created.
     *
     * @param createdAt the creation timestamp supplied by the service
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Sets when the product was last updated.
     *
     * @param updatedAt the latest update timestamp supplied by the service
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /** Returns when the product was last updated. */
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}