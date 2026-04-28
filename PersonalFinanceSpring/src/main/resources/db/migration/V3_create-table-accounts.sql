CREATE TABLE accounts
(
    account_id      SERIAL PRIMARY KEY,
    id              INTEGER REFERENCES users,
    account_balance INTEGER
);