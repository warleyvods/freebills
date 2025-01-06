ALTER TABLE transactions ADD COLUMN transaction_metadata JSONB;

ALTER TABLE transactions ADD COLUMN updated_at TIMESTAMP(6);

ALTER TABLE transactions DROP COLUMN bank_slip;
