CREATE TABLE IF NOT EXISTS users
(
    id          BIGSERIAL PRIMARY KEY,
    active      BOOLEAN      NOT NULL,
    admin       BOOLEAN      NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    email       VARCHAR(50)  NOT NULL UNIQUE,
    last_access TIMESTAMP(6),
    login       VARCHAR(50)  NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    password    VARCHAR(100) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL
);

CREATE TABLE IF NOT EXISTS accounts
(
    id           BIGSERIAL PRIMARY KEY,
    account_type VARCHAR(255),
    archived     BOOLEAN,
    bank_type    VARCHAR(255),
    created_at   TIMESTAMP(6) NOT NULL,
    dashboard    BOOLEAN,
    description  VARCHAR(255),
    user_id      BIGINT CONSTRAINT fk_user_id REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS transactions
(
    id                   BIGSERIAL PRIMARY KEY,
    amount               NUMERIC(38, 2),
    bank_slip            BOOLEAN,
    bar_code             VARCHAR(50),
    created_at           TIMESTAMP(6) NOT NULL,
    date                 DATE         NOT NULL,
    description          VARCHAR(255),
    paid                 BOOLEAN,
    transaction_category VARCHAR(255),
    transaction_type     VARCHAR(255),
    account_id           BIGINT CONSTRAINT fk_account_id REFERENCES accounts(id)
);

CREATE TABLE IF NOT EXISTS events
(
    id                   BIGSERIAL PRIMARY KEY,
    aggregate_id         BIGINT,
    created_at           timestamp(6) not null,
    event_type           varchar(255),
    old_transaction_data TEXT,
    transaction_data     TEXT
);

CREATE INDEX idx_created_at ON events (created_at);
CREATE INDEX idx_aggregate_id ON events (aggregate_id);
CREATE INDEX idx_event_type ON events (event_type);
