CREATE TABLE categories
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    color         VARCHAR(20) NOT NULL,
    created_at    TIMESTAMP(6) NOT NULL,
    updated_at    TIMESTAMP(6) NOT NULL,
    category_type VARCHAR(50)  NOT NULL,
    user_id       BIGSERIAL CONSTRAINT fk_user_category_id REFERENCES users (id)
);

ALTER TABLE transactions ADD COLUMN category_id BIGSERIAL CONSTRAINT fk_category_transaction_id REFERENCES categories (id)
