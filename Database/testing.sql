INSERT INTO `user`(`username`,`email`,`password`,`failed_payments`)
	VALUES('user','email@email','password1',0);
INSERT INTO `user`(`username`,`email`,`password`,`failed_payments`)
	VALUES('a','a@a','a',0);
    
INSERT INTO `optional`(`name`,`monthly_fee`)
	VALUES('optional1',10.50);    
INSERT INTO `optional`(`name`,`monthly_fee`)
	VALUES('optional2',20.99);
    
INSERT INTO `package`(`name`,`fixed_phone`)
	VALUES('package1',TRUE);    
INSERT INTO `package`(`name`,`fixed_phone`)
	VALUES('package2',FALSE);

INSERT INTO `package_to_optional`(`package_id`,`optional`)
	VALUES(1,'optional1');
INSERT INTO `package_to_optional`(`package_id`,`optional`)
	VALUES(1,'optional2');
INSERT INTO `package_to_optional`(`package_id`,`optional`)
	VALUES(2,'optional1');
    
INSERT INTO `mobile_phone`(`package_id`,`minutes`,`sms`,`minutes_fee`,`sms_fee`)
	VALUES(1,1000,100,20.00,5.00);
    
INSERT INTO `fixed_internet`(`package_id`,`gigabytes`,`gigabytes_fee`)
	VALUES(1,500,10.00);
    
INSERT INTO `mobile_internet`(`package_id`,`gigabytes`,`gigabytes_fee`)
	VALUES(1,40,20.00);
    
INSERT INTO `validity_period`(`package_id`,`months`,`monthly_fee`)
	VALUES(1,12,19.99);
INSERT INTO `validity_period`(`package_id`,`months`,`monthly_fee`)
	VALUES(1,24,29.99);
INSERT INTO `validity_period`(`package_id`,`months`,`monthly_fee`)
	VALUES(2,24,39.99);
    
INSERT INTO `order`(`creation_time`,`activation_date`,`total`,`months`,`user`,`package_id`)
	VALUES(CURRENT_TIMESTAMP(),'2023-01-01',150,12,'user',1);
INSERT INTO `order`(`creation_time`,`activation_date`,`total`,`months`,`user`,`package_id`)
	VALUES(CURRENT_TIMESTAMP(),'2023-02-01',150,12,'user',1);
INSERT INTO `order`(`creation_time`,`activation_date`,`total`,`months`,`user`,`package_id`)
	VALUES(CURRENT_TIMESTAMP(),'2023-03-01',350,12,'user',1);
    
INSERT INTO `order_to_optional`(`order_id`,`optional`)
	VALUES(1,'optional1');
INSERT INTO `order_to_optional`(`order_id`,`optional`)
	VALUES(1,'optional2');
INSERT INTO `order_to_optional`(`order_id`,`optional`)
	VALUES(2,'optional1');

UPDATE `order` SET `accepted` = 0 WHERE `id` = 1;
UPDATE `order` SET `accepted` = 0 WHERE `id` = 2;
UPDATE `order` SET `accepted` = 0 WHERE `id` = 3;
UPDATE `order` SET `accepted` = 1 WHERE `id` = 1;
UPDATE `order` SET `accepted` = 1 WHERE `id` = 2;

INSERT INTO `order`(`creation_time`,`activation_date`,`total`,`months`,`user`,`package_id`,`accepted`)
	VALUES(CURRENT_TIMESTAMP(),'2023-01-01',150,12,'user',1,TRUE);
INSERT INTO `order_to_optional`(`order_id`,`optional`)
	VALUES(1,'optional1');
INSERT INTO `order_to_optional`(`order_id`,`optional`)
	VALUES(1,'optional2');

SELECT *
FROM `user`;
SELECT *
FROM `optional`;
SELECT *
FROM `package`;
SELECT *
FROM `package_to_optional`;
SELECT *
FROM `order_to_optional`;
SELECT *
FROM `order`;
SELECT *
FROM `validity_period`;
SELECT *
FROM `suspended_order`;
SELECT *
FROM `activation_schedule`;
SELECT *
FROM `schedule_to_optional`;
SELECT *
FROM `auditing_table`;
SELECT *
FROM `insolvent_user`;
SELECT *
FROM `package_sales`;
SELECT *
FROM `validity_period_sales`;
SELECT *
FROM `optional_sales`;
