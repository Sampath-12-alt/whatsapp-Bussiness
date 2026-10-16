// This repository provides database operations for conversations.
// Its queries include the business ID so APIs can keep conversations scoped to their owner.
package com.platform.repository;

import com.platform.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findFirstByBusinessIdAndCustomerIdAndStatus(
            Long businessId, Long customerId, String status);

    List<Conversation> findAllByBusinessIdOrderByUpdatedAtDesc(Long businessId);

    Optional<Conversation> findByIdAndBusinessId(
            Long conversationId, Long businessId);
}
