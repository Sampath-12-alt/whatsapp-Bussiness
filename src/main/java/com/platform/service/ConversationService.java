// This service handles conversations and their messages.
// It checks business ownership and uses repositories to save and retrieve records.
package com.platform.service;

import com.platform.entity.Conversation;
import com.platform.entity.Customer;
import com.platform.entity.Message;
import com.platform.repository.ConversationRepository;
import com.platform.repository.CustomerRepository;
import com.platform.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ConversationService {

    private static final String OPEN = "OPEN";
    private static final String CUSTOMER = "CUSTOMER";
    private static final String BUSINESS = "BUSINESS";

    private final ConversationRepository conversationRepository;
    private final CustomerRepository customerRepository;
    private final MessageRepository messageRepository;

    // Spring injects the repositories used to check ownership and access stored conversations and messages.
    public ConversationService(
            ConversationRepository conversationRepository,
            CustomerRepository customerRepository,
            MessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.customerRepository = customerRepository;
        this.messageRepository = messageRepository;
    }

    /**
     * Returns the customer's open conversation or creates it when none exists.
     *
     * @param businessId ID of the business that owns the conversation
     * @param customerId ID of a customer belonging to that business
     * @return the existing or newly created open conversation
     */
    @Transactional
    public Conversation findOrCreateOpenConversation(Long businessId, Long customerId) {
        Customer customer = customerRepository
                .findByIdAndBusinessIdForUpdate(customerId, businessId)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Customer not found for this business"));

        return conversationRepository
                .findFirstByBusinessIdAndCustomerIdAndStatus(
                        businessId, customer.getId(), OPEN)
                .orElseGet(() -> createOpenConversation(businessId, customerId));
    }

    /**
     * Lists all conversations owned by a business.
     *
     * @param businessId ID of the business whose conversations are requested
     * @return conversations ordered by their most recent update
     */
    @Transactional(readOnly = true)
    public List<Conversation> getConversations(Long businessId) {
        return conversationRepository.findAllByBusinessIdOrderByUpdatedAtDesc(businessId);
    }

    /**
     * Finds one conversation only when it belongs to the requested business.
     *
     * @param businessId ID of the business that must own the conversation
     * @param conversationId ID of the conversation to find
     * @return the matching conversation
     * @throws ResponseStatusException with HTTP 404 when it does not belong to the business
     */
    @Transactional(readOnly = true)
    public Conversation getConversation(Long businessId, Long conversationId) {
        return conversationRepository.findByIdAndBusinessId(conversationId, businessId)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Conversation not found"));
    }

    /**
     * Lists messages after checking that the conversation belongs to the business.
     *
     * @param businessId ID of the business that must own the conversation
     * @param conversationId ID of the conversation whose messages are requested
     * @return messages in the order they were created
     */
    @Transactional(readOnly = true)
    public List<Message> getMessages(Long businessId, Long conversationId) {
        getConversation(businessId, conversationId);
        return messageRepository
                .findAllByConversationIdOrderByCreatedAtAscIdAsc(conversationId);
    }

    /**
     * Stores a message in a conversation owned by the business.
     *
     * @param businessId ID of the business that must own the conversation
     * @param conversationId ID of the conversation to receive the message
     * @param senderType CUSTOMER or BUSINESS
     * @param text non-blank message text
     * @param providerMessageId optional ID supplied by a messaging provider
     * @param requestedStatus optional allowed delivery status
     * @return the saved message
     */
    @Transactional
    public Message createMessage(
            Long businessId,
            Long conversationId,
            String senderType,
            String text,
            String providerMessageId,
            String requestedStatus) {
        Conversation conversation = getConversation(businessId, conversationId);

        if (providerMessageId != null
                && messageRepository.existsByProviderMessageId(providerMessageId)) {
            throw new ResponseStatusException(
                    CONFLICT, "A message with this provider ID already exists");
        }

        LocalDateTime now = LocalDateTime.now();
        Message message = new Message();
        message.setConversationId(conversationId);
        message.setSenderType(senderType);
        message.setMessage(text);
        message.setProviderMessageId(providerMessageId);
        message.setStatus(requestedStatus == null
                ? defaultStatus(senderType)
                : requestedStatus);
        message.setCreatedAt(now);

        conversation.setUpdatedAt(now);
        conversationRepository.save(conversation);

        return messageRepository.save(message);
    }

    // Creates a new open conversation after the owning customer has been locked and checked.
    private Conversation createOpenConversation(Long businessId, Long customerId) {
        LocalDateTime now = LocalDateTime.now();
        Conversation conversation = new Conversation();
        conversation.setBusinessId(businessId);
        conversation.setCustomerId(customerId);
        conversation.setStatus(OPEN);
        conversation.setCreatedAt(now);
        conversation.setUpdatedAt(now);
        return conversationRepository.save(conversation);
    }

    // Customer messages are received; business messages are ready to be sent.
    private String defaultStatus(String senderType) {
        return CUSTOMER.equals(senderType) ? "RECEIVED" : "SENT";
    }
}
