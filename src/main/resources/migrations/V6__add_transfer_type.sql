CREATE TABLE IF NOT EXISTS transfers
(
    id                BIGSERIAL PRIMARY KEY,
    observation       VARCHAR(255),
    description       VARCHAR(255),
    amount            NUMERIC(38, 2),
    date              DATE,
    transfer_category VARCHAR(255),
    from_account_id   BIGINT       NOT NULL CONSTRAINT fk_from_account REFERENCES accounts(id) ON DELETE CASCADE,
    to_account_id     BIGINT       NOT NULL CONSTRAINT fk_to_account REFERENCES accounts(id) ON DELETE CASCADE,
    updated_at        TIMESTAMP(6) NOT NULL,
    created_at        TIMESTAMP(6) NOT NULL
);

ALTER TABLE events ADD COLUMN IF NOT EXISTS transfer_json_data JSONB;
ALTER TABLE events ADD COLUMN IF NOT EXISTS old_transfer_json_data JSONB;
ALTER TABLE events ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP(6);
