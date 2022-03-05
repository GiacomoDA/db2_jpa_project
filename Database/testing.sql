INSERT INTO `user`(`username`,`email`,`password`,`failed_payments`)
	VALUES('user','email@email','password1',0);
    
INSERT INTO `optional`(`name`,`monthly_fee`)
	VALUES('optional1',10.00);    
INSERT INTO `optional`(`name`,`monthly_fee`)
	VALUES('optional2',20.00);
    
INSERT INTO `package`(`name`)
	VALUES('package1');    
INSERT INTO `package`(`name`)
	VALUES('package2');
    
INSERT INTO `package_to_optional`(`package_id`,`optional`)
	VALUES(1,'optional1');
INSERT INTO `package_to_optional`(`package_id`,`optional`)
	VALUES(1,'optional2');
    
INSERT INTO `validity_period`(`package_id`,`months`,`monthly_fee`)
	VALUES(1,12,19.99);
    
INSERT INTO `order`(`creation_time`,`activation_date`,`total`,`months`,`user`,`package_id`)
	VALUES(CURRENT_TIMESTAMP(),'2022-01-01',150,12,'user',1);
    
INSERT INTO `order_to_optional`(`order_id`,`optional`)
	VALUES(1,'optional1');
INSERT INTO `order_to_optional`(`order_id`,`optional`)
	VALUES(1,'optional2');

UPDATE `order` SET `accepted` = 1 WHERE `id` = 1;
    
SELECT *
FROM `user`;
SELECT *
FROM `optional`;
SELECT *
FROM `package`;
SELECT *
FROM `package_to_optional`;
SELECT *
FROM `validity_period`;
SELECT *
FROM `order`;
SELECT *
FROM `package_sales`;
SELECT *
FROM `order_to_optional`;