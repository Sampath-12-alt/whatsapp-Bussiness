// This controller exposes business-scoped conversation and message endpoints.
// It validates request DTOs and converts saved entities into response DTOs.
package com.platform.controller;

import com.platform.dto.ConversationDetailsResponse;
import com.platform.dto.ConversationResponse;
import com.platform.dto.MessageRequest;
import com.platform.dto.MessageResponse;
import com.platform.entity.Conversation;
import com.platform.entity.Message;
import com.platform.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/businesses/{businessId}")
public class ConversationController {

    private final ConversationService conversationService;

    // Spring supplies the service through constructor-based dependency injection.
    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    /**
     * Finds or creates an open conversation for a customer of the business.
     *
     * @param businessId ID of the business that owns the conversation
     * @param customerId ID of a customer belonging to that business
     * @return the existing or newly created conversation
     */
    @PostMapping("/customers/{customerId}/conversations")
    public ConversationResponse findOrCreateOpenConversation(
            @PathVariable Long businessId,
            @PathVariable Long customerId) {
        return toConversationResponse(
                conversationService.findOrCreateOpenConversation(businessId, customerId));
    }

    /**
     * Lists conversations for the business.
     *
     * @param businessId ID of the business whose conversations are requested
     * @return conversation summaries
     */
    @GetMapping("/conversations")
    public List<ConversationResponse> getConversations(
            @PathVariable Long businessId) {
        return conversationService.getConversations(businessId).stream()
                .map(this::toConversationResponse)
                .toList();
    }

    /**
     * Gets a business-owned conversation together with its messages.
     *
     * @param businessId ID of the business that must own the conversation
     * @param conversationId ID of the conversation to retrieve
     * @return conversation details and all messages in creation order
     */
    @GetMapping("/conversations/{conversationId}")
    public ConversationDetailsResponse getConversation(
            @PathVariable Long businessId,
            @PathVariable Long conversationId) {
        Conversation conversation =
                conversationService.getConversation(businessId, conversationId);
        List<MessageResponse> messages =
                conversationService.getMessages(businessId, conversationId).stream()
                        .map(this::toMessageResponse)
                        .toList();

        return new ConversationDetailsResponse(
                conversation.getId(),
                conversation.getBusinessId(),
                conversation.getCustomerId(),
                conversation.getStatus(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt(),
                messages);
    }

    /**
     * Lists messages for a conversation after checking its business ownership.
     *
     * @param businessId ID of the business that must own the conversation
     * @param conversationId ID of the conversation
     * @return messages in creation order
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public List<MessageResponse> getMessages(
            @PathVariable Long businessId,
            @PathVariable Long conversationId) {
        return conversationService.getMessages(businessId, conversationId).stream()
                .map(this::toMessageResponse)
                .toList();
    }

    /**
     * Stores a customer or business message in a business-owned conversation.
     *
     * @param businessId ID of the business that must own the conversation
     * @param conversationId ID of the conversation
     * @param request validated message content and optional delivery details
     * @return the saved message response
     */
    @PostMapping("/conversations/{conversationId}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    public MessageResponse createMessage(
            @PathVariable Long businessId,
            @PathVariable Long conversationId,
            @Valid @RequestBody MessageRequest request) {
        Message message = conversationService.createMessage(
                businessId,
                conversationId,
                request.getSenderType(),
                request.getMessage(),
                request.getProviderMessageId(),
                request.getStatus());
        return toMessageResponse(message);
    }

    // Copies only API response fields instead of exposing JPA entities directly.
    private ConversationResponse toConversationResponse(Conversation conversation) {
        return new ConversationResponse(
                conversation.getId(),
                conversation.getBusinessId(),
                conversation.getCustomerId(),
                conversation.getStatus(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt());
    }

    // Copies stored message fields into the message response DTO.
    private MessageResponse toMessageResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getConversationId(),
                message.getSenderType(),
                message.getMessage(),
                message.getProviderMessageId(),
                message.getStatus(),
                message.getCreatedAt());
    }
}
