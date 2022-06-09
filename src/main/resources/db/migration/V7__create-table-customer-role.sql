CREATE TABLE customer_role (
    customer_id INT NOT NULL,
    role VARCHAR(255) NOT NULL,
    CONSTRAINT FK_CUSTOMER_ROLE_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id)
);