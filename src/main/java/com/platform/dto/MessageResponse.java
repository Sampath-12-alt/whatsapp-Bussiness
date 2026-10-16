// This DTO contains message details returned by conversation APIs.
// It prevents the API from exposing the JPA entity directly.
package com.platform.dto;

import java.time.LocalDateTime;

public class MessageResponse {

    private final Long id;
    private final Long conversationId;
    private final String senderType;
    private final String message;
    private final String providerMessageId;
    private final String status;
    private final LocalDateTime createdAt;

    public MessageResponse(
            Long id,
            Long conversationId,
            String senderType,
            String message,
            String providerMessageId,
            String status,
            LocalDateTime createdAt) {
        this.id = id;
        this.conversationId = conversationId;
        this.senderType = senderType;
        this.message = message;
        this.providerMessageId = providerMessageId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public String getSenderType() {
        return senderType;
    }

    public String getMessage() {
        return message;
    }

    public String getProviderMessageId() {
        return providerMessageId;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
