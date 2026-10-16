// This DTO contains the customer fields returned by the API.
// It lets the API return customer data without exposing the JPA entity.
package com.platform.dto;

import java.time.LocalDateTime;

// Contains the customer details returned to API callers.
public class CustomerResponse {

    private final Long id;
    private final Long businessId;
    private final String name;
    private final String phone;
    private final String address;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CustomerResponse(
            Long id,
            Long businessId,
            String name,
            String phone,
            String address,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.businessId = businessId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
