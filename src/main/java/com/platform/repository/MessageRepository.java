// This repository provides database operations for conversation messages.
// It returns messages in time order and checks provider IDs for duplicates.
package com.platform.repository;

import com.platform.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findAllByConversationIdOrderByCreatedAtAscIdAsc(Long conversationId);

    boolean existsByProviderMessageId(String providerMessageId);
}
