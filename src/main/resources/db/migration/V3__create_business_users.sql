CREATE TABLE business_users (
    business_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    PRIMARY KEY (business_id, user_id),

    FOREIGN KEY (business_id) REFERENCES businesses(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);