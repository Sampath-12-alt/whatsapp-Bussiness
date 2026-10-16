// This DTO summarizes a conversation without returning its database entity.
// Messages are retrieved through the conversation's messages endpoint.
package com.platform.dto;

import java.time.LocalDateTime;

public class ConversationResponse {

    private final Long id;
    private final Long businessId;
    private final Long customerId;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ConversationResponse(
            Long id,
            Long businessId,
            Long customerId,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt) {
        this.id = id;
        this.businessId = businessId;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
