CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    account_id BIGINT REFERENCES accounts,
    value BIGINT NOT NULL,
    transaction_time TIMESTAMP NOT NULL,
    category varchar(255) NOT NULL,
    description TEXT
);