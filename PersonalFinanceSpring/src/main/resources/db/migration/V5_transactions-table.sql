ALTER TABLE transactions
    ALTER COLUMN category SET DATA TYPE BIGINT;

ALTER TABLE transactions
    ADD CONSTRAINT categories FOREIGN KEY (category) references categories;