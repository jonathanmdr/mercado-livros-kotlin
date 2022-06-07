CREATE TABLE customer (
    id INT AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT PK_CUSTOMER PRIMARY KEY (id),
    CONSTRAINT UK_CUSTOMER_EMAIL UNIQUE KEY (email)
);