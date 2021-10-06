-- *** Product catalog ***
CREATE TABLE Product
(
  id BIGSERIAL,
  modificationCounter INTEGER NOT NULL,
  title VARCHAR (500) NOT NULL,
  description VARCHAR (4000),
  price DECIMAL (16,2) NOT NULL,
  CONSTRAINT PK_Product PRIMARY KEY(id)
);
