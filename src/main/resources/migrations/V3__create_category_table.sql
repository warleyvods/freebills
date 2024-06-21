CREATE TABLE category
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    color       VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);

ALTER TABLE transactions ADD COLUMN category_id BIGSERIAL CONSTRAINT fk_category_id REFERENCES category(id)