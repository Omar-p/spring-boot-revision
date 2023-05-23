CREATE SEQUENCE customer_id_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE customer (
      age INT NOT NULL CHECK ( age > 0 AND age < 120 ),
      id BIGINT NOT NULL DEFAULT nextval('customer_id_sequence'),
      email TEXT NOT NULL,
      name TEXT NOT NULL,
      PRIMARY KEY (id),
      CONSTRAINT customer_email_unique UNIQUE (email)
);

ALTER SEQUENCE customer_id_sequence
      OWNED BY customer.id;