CREATE TABLE budgets
(
    id       SERIAL PRIMARY KEY,
    budget_limit Integer NOT NULL,
    month Integer NOT NULL,
    account_id INTEGER REFERENCES users,
    category_id INTEGER REFERENCES categories
);