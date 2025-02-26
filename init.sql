CREATE TABLE IF NOT EXISTS products
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255)            NOT NULL,
    description TEXT,
    price       DECIMAL(10, 2)          NOT NULL,
    created_at  TIMESTAMP DEFAULT now() NOT NULL,
    updated_at  TIMESTAMP DEFAULT now() NOT NULL
);

CREATE TABLE IF NOT EXISTS complaints
(
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id        INT                            NOT NULL,
    content           TEXT                           NOT NULL,
    created_at        TIMESTAMP        DEFAULT now() NOT NULL,
    submitter_name    VARCHAR(255)                   NOT NULL,
    submitter_surname VARCHAR(255)                   NOT NULL,
    country           VARCHAR(100)                   NOT NULL,
    submission_count  INT              DEFAULT 1     NOT NULL,
    CONSTRAINT chk_submission_count CHECK (submission_count > 0),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE
);

INSERT INTO products (name, description, price, created_at, updated_at)
VALUES ('Gaming Monitor', 'Ultra HD 144Hz gaming monitor with HDR support', 1899.99, NOW(), NOW()),
       ('Mechanical Keyboard', 'RGB backlit mechanical keyboard with blue switches', 499.50, NOW(), NOW()),
       ('Wireless Mouse', 'Ergonomic wireless mouse with adjustable DPI', 249.99, NOW(), NOW());

INSERT INTO complaints (product_id, content, created_at, submitter_name, submitter_surname, country, submission_count)
VALUES (1, 'The monitor has dead pixels after a week of use.', NOW(), 'Alice', 'Cooper', 'USA', 1),
       (2, 'Some keys on the keyboard stopped responding.', NOW(), 'David', 'Miller', 'Canada', 1),
       (3, 'The mouse battery drains too quickly.', NOW(), 'Sophia', 'Williams', 'UK', 1);