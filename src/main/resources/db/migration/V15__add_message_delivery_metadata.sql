ALTER TABLE messages
    ADD COLUMN provider_message_id VARCHAR(255),
    ADD COLUMN status VARCHAR(50) NOT NULL DEFAULT 'RECEIVED';

ALTER TABLE messages
    ADD CONSTRAINT chk_messages_status
    CHECK (status IN ('RECEIVED', 'SENT', 'DELIVERED', 'READ', 'FAILED'));

CREATE UNIQUE INDEX uq_messages_provider_message_id
    ON messages (provider_message_id)
    WHERE provider_message_id IS NOT NULL;

CREATE UNIQUE INDEX uq_conversations_open_business_customer
    ON conversations (business_id, customer_id)
    WHERE status = 'OPEN';
