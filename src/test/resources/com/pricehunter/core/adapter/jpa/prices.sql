SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE `price`;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO PRICE(id, value, product_id)
VALUES (100, 123.3, 100)
