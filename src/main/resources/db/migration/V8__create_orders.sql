CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    business_id BIGINT NOT NULL,
    customer_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    total_amount NUMERIC(12,2) NOT NULL DEFAULT 0,
    payment_status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (business_id) REFERENCES businesses(id),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);