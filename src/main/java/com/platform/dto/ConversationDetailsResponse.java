// This DTO returns conversation details together with its messages.
// It keeps the API response separate from both JPA entities.
package com.platform.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ConversationDetailsResponse {

    private final Long id;
    private final Long businessId;
    private final Long customerId;
    private final String status;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final List<MessageResponse> messages;

    public ConversationDetailsResponse(
            Long id,
            Long businessId,
            Long customerId,
            String status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            List<MessageResponse> messages) {
        this.id = id;
        this.businessId = businessId;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.messages = messages;
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

    public List<MessageResponse> getMessages() {
        return messages;
    }
}
