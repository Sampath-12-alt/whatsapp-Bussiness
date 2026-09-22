CREATE TABLE conversations (
    id BIGSERIAL PRIMARY KEY,
    business_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'OPEN',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (business_id) REFERENCES businesses(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);