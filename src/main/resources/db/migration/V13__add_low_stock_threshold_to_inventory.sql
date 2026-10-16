ALTER TABLE inventory
ADD COLUMN low_stock_threshold NUMERIC(12, 2) NOT NULL DEFAULT 5
CHECK (low_stock_threshold >= 0);