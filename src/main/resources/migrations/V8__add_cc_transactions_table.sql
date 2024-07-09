CREATE TABLE IF NOT EXISTS cc_transactions
(
    id BIGSERIAL PRIMARY KEY,
    amount NUMERIC(38, 2),
    date  DATE NOT NULL,
    description VARCHAR(255),
    transaction_type VARCHAR(30),
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    category_id BIGINT NOT NULL CONSTRAINT fk_cc_category REFERENCES categories(id) ON DELETE CASCADE,
    credit_card_id BIGINT NOT NULL CONSTRAINT fk_cc REFERENCES credit_card(id) ON DELETE CASCADE
)