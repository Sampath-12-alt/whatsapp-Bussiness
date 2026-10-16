// This DTO validates the message fields accepted by the send/store-message endpoint.
// It keeps API input separate from the Message database entity.
package com.platform.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MessageRequest {

    @NotBlank(message = "Sender type is required")
    @Pattern(regexp = "CUSTOMER|BUSINESS", message = "Sender type must be CUSTOMER or BUSINESS")
    private String senderType;

    @NotBlank(message = "Message text is required")
    private String message;

    @Size(max = 255, message = "Provider message ID must be 255 characters or fewer")
    private String providerMessageId;

    @Pattern(
            regexp = "RECEIVED|SENT|DELIVERED|READ|FAILED",
            message = "Status must be RECEIVED, SENT, DELIVERED, READ, or FAILED")
    private String status;

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProviderMessageId() {
        return providerMessageId;
    }

    public void setProviderMessageId(String providerMessageId) {
        this.providerMessageId = providerMessageId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
