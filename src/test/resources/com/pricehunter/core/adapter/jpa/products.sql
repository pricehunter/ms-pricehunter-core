SET FOREIGN_KEY_CHECKS=0;
TRUNCATE TABLE `product`;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO PRODUCT(id, name, description)
values (100, 'aProduct', 'aDescription');
