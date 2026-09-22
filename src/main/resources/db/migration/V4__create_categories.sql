CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    business_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,

    FOREIGN KEY (business_id) REFERENCES businesses(id)
);