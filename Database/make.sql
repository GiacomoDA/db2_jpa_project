DROP DATABASE IF EXISTS `db_telco_project`;
CREATE DATABASE IF NOT EXISTS `db_telco_project`;
USE `db_telco_project`;

SET FOREIGN_KEY_CHECKS=0;
SET UNIQUE_CHECKS=0;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
	`username` varchar(30) NOT NULL,
    `email` varchar(30) NOT NULL,
    `password` varchar(30) NOT NULL,
    `failed_payments` smallint NOT NULL,
    CHECK (`failed_payments` >= 0),
    PRIMARY KEY (`username`)
);


DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
	`username` varchar(30) NOT NULL,
    `email` varchar(30) NOT NULL,
    `password` varchar(30) NOT NULL,
    PRIMARY KEY (`username`)
);


DROP TABLE IF EXISTS `optional`;
CREATE TABLE `optional` (
	`name` varchar(30) NOT NULL,
    `monthly_fee` decimal(5,2) NOT NULL,
    PRIMARY KEY (name),
    CHECK (`monthly_fee` >= 0.00)
);


DROP TABLE IF EXISTS `package`;
CREATE TABLE `package` (
	`id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(30) NOT NULL,
    PRIMARY KEY (`id`)
);


DROP TABLE IF EXISTS `validity_period`;
CREATE TABLE `validity_period` (
    `package_id` int NOT NULL,
	`months` int NOT NULL,
    `monthly_fee` decimal(5,2) NOT NULL,
    PRIMARY KEY (`months`, `package_id`),
    FOREIGN KEY (`package_id`) REFERENCES `package` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (`monthly_fee` >= 0.00)
);

DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
	`id` int NOT NULL AUTO_INCREMENT,
    `creation_time` datetime NOT NULL,
    `activation_date` date NOT NULL,
    `total` int NOT NULL,
    `accepted` bit,
    `months` int,
    `user` varchar(30),
    `package_id` int,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`package_id`) REFERENCES `package` (`id`) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (`months`,`package_id`) REFERENCES `validity_period` (`months`,`package_id`) ON DELETE NO ACTION ON UPDATE CASCADE,
    FOREIGN KEY (`user`) REFERENCES `user` (`username`) ON DELETE SET NULL ON UPDATE CASCADE,
    CHECK (`total` >= 0.00),
    CHECK (`total` >= 0)
);


DROP TABLE IF EXISTS `package_to_optional`;
CREATE TABLE `package_to_optional` (
    `package_id` int NOT NULL,
	`optional` varchar(30) NOT NULL,
    PRIMARY KEY (`package_id`,`optional`),
    FOREIGN KEY (`package_id`) REFERENCES `package` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`optional`) REFERENCES `optional` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `order_to_optional`;
CREATE TABLE `order_to_optional` (
    `order_id` int NOT NULL,
	`optional` varchar(30) NOT NULL,
    PRIMARY KEY (`order_id`,`optional`),
    FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`optional`) REFERENCES `optional` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `service`;
CREATE TABLE `service` (
    `id` int NOT NULL AUTO_INCREMENT,
	`package_id` int NOT NULL,
    PRIMARY KEY (`id`, `package_id`),
    FOREIGN KEY (`package_id`) REFERENCES `package` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
); 


DROP TABLE IF EXISTS `fixed_phone`;
CREATE TABLE `fixed_phone` (
    `id` int NOT NULL AUTO_INCREMENT,
	`package_id` int NOT NULL,
    PRIMARY KEY (`id`, `package_id`),
    FOREIGN KEY (`id`,`package_id`) REFERENCES `service` (`id`,`package_id`) ON DELETE CASCADE ON UPDATE CASCADE
); 


DROP TABLE IF EXISTS `mobile_phone`;
CREATE TABLE `mobile_phone` (
    `id` int NOT NULL AUTO_INCREMENT,
	`package_id` int NOT NULL,
    `minutes` smallint NOT NULL,
    `sms` smallint NOT NULL,
    `minutes_fee` decimal(5,2) NOT NULL,
    `sms_fee` decimal(5,2) NOT NULL,
    PRIMARY KEY (`id`, `package_id`),
    FOREIGN KEY (`id`,`package_id`) REFERENCES `service` (`id`,`package_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (`minutes` >= 0),
    CHECK (`sms` >= 0),
    CHECK (`minutes_fee` >= 0.00),
    CHECK (`sms_fee` >= 0.00)
); 


DROP TABLE IF EXISTS `fixed_internet`;
CREATE TABLE `fixed_internet` (
    `id` int NOT NULL AUTO_INCREMENT,
	`package_id` int NOT NULL,
    `gigabytes` smallint NOT NULL,
    `gigabytes_fee` decimal(5,2) NOT NULL,
    PRIMARY KEY (`id`, `package_id`),
    FOREIGN KEY (`id`,`package_id`) REFERENCES `service` (`id`,`package_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (`gigabytes` >= 0),
    CHECK (`gigabytes_fee` >= 0.00)
); 


DROP TABLE IF EXISTS `mobile_internet`;
CREATE TABLE `mobile_internet` (
    `id` int NOT NULL AUTO_INCREMENT,
	`package_id` int NOT NULL,
    `gigabytes` smallint NOT NULL,
    `gigabytes_fee` decimal(5,2) NOT NULL,
    PRIMARY KEY (`id`, `package_id`),
    FOREIGN KEY (`id`,`package_id`) REFERENCES `service` (`id`,`package_id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (`gigabytes` >= 0),
    CHECK (`gigabytes_fee` >= 0.00)
);


DROP TABLE IF EXISTS `activation_schedule`;
CREATE TABLE `activation_schedule` (
    `user` varchar(30) NOT NULL,
	`package_id` int NOT NULL,
    `activation_date` date NOT NULL,
    `deactivation_date` date NOT NULL,
    PRIMARY KEY (`user`,`package_id`,`activation_date`),
    FOREIGN KEY (`user`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`package_id`) REFERENCES `package` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `schedule_to_optional`;
CREATE TABLE `schedule_to_optional` (
    `user` varchar(30) NOT NULL,
	`package_id` int NOT NULL,
    `activation_date` date NOT NULL,
	`optional` varchar(30) NOT NULL,
    PRIMARY KEY (`user`,`package_id`,`activation_date`,`optional`),
    FOREIGN KEY (`user`,`package_id`,`activation_date`) REFERENCES `activation_schedule` (`user`,`package_id`,`activation_date`) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (`optional`) REFERENCES `optional` (`name`) ON DELETE CASCADE ON UPDATE CASCADE
);


DROP TABLE IF EXISTS `auduting_table`;
CREATE TABLE `auduting_table` (
    `user` varchar(30) NOT NULL,
	`email` varchar(30) NOT NULL,
    `amount` int NOT NULL,
    `last_order` datetime NOT NULL,
    PRIMARY KEY (`user`),
    FOREIGN KEY (`user`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
);


-- sales report


DROP TABLE IF EXISTS `package_sales`;
CREATE TABLE `package_sales` (
	`id` int NOT NULL,
	`name` varchar(30) NOT NULL,
    `sales` int NOT NULL,
    `sales_with_optionals` int NOT NULL,
    `optionals_sales` int NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`id`) REFERENCES `package`(`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
    CHECK (`sales` > 0),
    CHECK (`sales_with_optionals` > 0),
    CHECK (`optionals_sales` > 0)
);


DROP TABLE IF EXISTS `validity_period_sales`;
CREATE TABLE `validity_period_sales` (
	`package_id` int NOT NULL,
    `months` int NOT NULL,
    `monthly_fee` decimal(5,2) NOT NULL,
    `sales` int NOT NULL,
    PRIMARY KEY (`package_id`, `months`),
    FOREIGN KEY (`package_id`, `months`) REFERENCES `validity_period`(`package_id`, `months`) ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (`sales` > 0),
    CHECK (`monthly_fee` > 0.00)
);


DROP TABLE IF EXISTS `optional_sales`;
CREATE TABLE `optional_sales` (
	`name` varchar(30) NOT NULL,
    `sales` int NOT NULL,
    PRIMARY KEY (`name`),
    FOREIGN KEY (`name`) REFERENCES `optional`(`name`) ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (`sales` > 0)
);


DROP TABLE IF EXISTS `insolvent_user`;
CREATE TABLE `insolvent_user` (
	`user` varchar(30) NOT NULL,
    `email` varchar(30) NOT NULL,
    `failed_payments` smallint NOT NULL,
    PRIMARY KEY (`user`),
    FOREIGN KEY (`user`) REFERENCES `user`(`username`) ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (`failed_payments` > 0)
);


DROP TABLE IF EXISTS `suspended_order`;
CREATE TABLE `suspended_order` (
	`id` int NOT NULL,
    `user` varchar(30) NOT NULL,
    `total` int NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`id`) REFERENCES `order`(`id`) ON DELETE CASCADE ON UPDATE CASCADE,
    CHECK (`total` > 0)
);


SET FOREIGN_KEY_CHECKS=1;
SET UNIQUE_CHECKS=1;
