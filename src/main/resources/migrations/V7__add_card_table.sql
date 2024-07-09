CREATE TABLE IF NOT EXISTS credit_card (
    id                BIGSERIAL   PRIMARY KEY,
    card_limit        NUMERIC(38, 2),
    description       VARCHAR(60),
    expiration_day    INT,
    closing_day       INT,
    archived          BOOLEAN,
    card_flag         VARCHAR(30),
    updated_at        TIMESTAMP(6) NOT NULL,
    created_at        TIMESTAMP(6) NOT NULL,
    account_id        BIGINT NOT NULL CONSTRAINT fk_card_account REFERENCES accounts(id) ON DELETE CASCADE
);