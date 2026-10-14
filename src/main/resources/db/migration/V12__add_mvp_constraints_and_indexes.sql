-- ============================================================
-- V12: MVP Constraints and Indexes
-- ============================================================

-- ------------------------------------------------------------
-- Categories
-- ------------------------------------------------------------

ALTER TABLE categories
ADD CONSTRAINT uq_categories_business_name
UNIQUE (business_id, name);


-- ------------------------------------------------------------
-- Products
-- ------------------------------------------------------------

ALTER TABLE products
ADD CONSTRAINT chk_products_price_non_negative
CHECK (price >= 0);

ALTER TABLE products
ADD CONSTRAINT chk_products_name_not_empty
CHECK (length(trim(name)) > 0);


-- ------------------------------------------------------------
-- Inventory
-- ------------------------------------------------------------

ALTER TABLE inventory
ADD CONSTRAINT chk_inventory_quantity_non_negative
CHECK (quantity >= 0);


-- ------------------------------------------------------------
-- Customers
-- ------------------------------------------------------------

ALTER TABLE customers
ADD CONSTRAINT uq_customers_business_phone
UNIQUE (business_id, phone);


-- ------------------------------------------------------------
-- Orders
-- ------------------------------------------------------------

ALTER TABLE orders
ADD CONSTRAINT chk_orders_total_non_negative
CHECK (total_amount >= 0);

ALTER TABLE orders
ADD CONSTRAINT chk_orders_status
CHECK (
    status IN (
        'PENDING',
        'CONFIRMED',
        'PREPARING',
        'READY',
        'DELIVERED',
        'CANCELLED'
    )
);

ALTER TABLE orders
ADD CONSTRAINT chk_orders_payment_status
CHECK (
    payment_status IN (
        'PENDING',
        'PROCESSING',
        'PAID',
        'FAILED'
    )
);


-- ------------------------------------------------------------
-- Order Items
-- ------------------------------------------------------------

ALTER TABLE order_items
ADD CONSTRAINT chk_order_items_quantity_positive
CHECK (quantity > 0);

ALTER TABLE order_items
ADD CONSTRAINT chk_order_items_unit_price_non_negative
CHECK (unit_price >= 0);

ALTER TABLE order_items
ADD CONSTRAINT chk_order_items_total_price_non_negative
CHECK (total_price >= 0);


-- ------------------------------------------------------------
-- Conversations
-- ------------------------------------------------------------

ALTER TABLE conversations
ADD CONSTRAINT chk_conversations_status
CHECK (
    status IN (
        'OPEN',
        'CLOSED'
    )
);


-- ------------------------------------------------------------
-- Messages
-- ------------------------------------------------------------

ALTER TABLE messages
ADD CONSTRAINT chk_messages_sender_type
CHECK (
    sender_type IN (
        'CUSTOMER',
        'BUSINESS'
    )
);


-- ============================================================
-- Indexes
-- ============================================================

CREATE INDEX idx_categories_business_id
ON categories (business_id);

CREATE INDEX idx_products_business_id
ON products (business_id);

CREATE INDEX idx_products_category_id
ON products (category_id);

CREATE INDEX idx_inventory_product_id
ON inventory (product_id);

CREATE INDEX idx_customers_business_id
ON customers (business_id);

CREATE INDEX idx_orders_business_id
ON orders (business_id);

CREATE INDEX idx_orders_customer_id
ON orders (customer_id);

CREATE INDEX idx_order_items_order_id
ON order_items (order_id);

CREATE INDEX idx_order_items_product_id
ON order_items (product_id);

CREATE INDEX idx_conversations_business_id
ON conversations (business_id);

CREATE INDEX idx_conversations_customer_id
ON conversations (customer_id);

CREATE INDEX idx_messages_conversation_id
ON messages (conversation_id);