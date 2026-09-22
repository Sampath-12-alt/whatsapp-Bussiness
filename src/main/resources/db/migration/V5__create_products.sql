CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    business_id BIGINT NOT NULL,
    category_id BIGINT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(12,2) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (business_id) REFERENCES businesses(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);