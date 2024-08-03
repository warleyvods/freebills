CREATE TABLE IF NOT EXISTS file_reference
(
    id                UUID PRIMARY KEY,
    file_name         VARCHAR(255),
    content_type      VARCHAR(255),
    content_length    BIGINT,
    temp              BOOLEAN DEFAULT TRUE,
    "type"            VARCHAR(255),
    created_at        TIMESTAMP(6) NOT NULL,
    updated_at        TIMESTAMP(6) NOT NULL
);