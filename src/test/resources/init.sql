CREATE TABLE IF NOT EXISTS products
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS complainers
(
    id                 SERIAL PRIMARY KEY,
    complainer_name    VARCHAR(255) NOT NULL,
    complainer_surname VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS complaints
(
    id                SERIAL PRIMARY KEY,
    product_id        INT                     NOT NULL,
    complainer_id     INT                     NOT NULL,
    content           TEXT                    NOT NULL,
    created_at        TIMESTAMP DEFAULT now() NOT NULL,
    complaint_country VARCHAR(100)            NOT NULL,
    complaint_count   INT       DEFAULT 1     NOT NULL,
    version           INT       DEFAULT 0     NOT NULL,
    CONSTRAINT chk_submission_count CHECK (complaint_count > 0),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_complainer FOREIGN KEY (complainer_id) REFERENCES complainers (id) ON DELETE CASCADE
);

INSERT INTO products (name)
VALUES ('Laptop Pro X'),
       ('Smartphone Ultra Y'),
       ('Wireless Headphones Z');

INSERT INTO complainers (complainer_name, complainer_surname)
VALUES ('John', 'Smith'),
       ('Alice', 'Johnson'),
       ('Michael', 'Brown');

INSERT INTO complaints (product_id, complainer_id, content, created_at, complaint_country, complaint_count, version)
VALUES (1, 1, 'The laptop overheats after 30 minutes of usage.', '2023-03-15T10:15:30+00:00', 'US', 1, 0),
       (2, 2, 'The smartphone battery drains too fast.', '2023-03-16T11:20:00+00:00', 'UK', 2, 0),
       (3, 3, 'The wireless headphones disconnect randomly.', '2023-03-17T12:30:15+00:00', 'Canada', 1, 0);
