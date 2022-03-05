USE `db_telco_project`;

DELIMITER //

-- when an order is accepted update the sales relative to the package sold
DROP TRIGGER IF EXISTS `update_package_sales`//
CREATE TRIGGER `update_package_sales` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `sales_w_o` int;
	DECLARE `opt_sales` int;
    SET `opt_sales` = (SELECT count(*) FROM `order_to_optional` WHERE `order_id` = NEW.`id`);
    SET `sales_w_o` = (SELECT count(DISTINCT(`order_id`)) FROM `order_to_optional` WHERE `order_id` = NEW.`id`);
	IF NEW.`accepted` = 1 THEN
		IF EXISTS (SELECT `id` FROM `package_sales` WHERE `id` = NEW.`package_id`) THEN
			UPDATE `package_sales`
				SET `sales` = `sales` + 1, `sales_with_optional` = `sales_with_optional` + `sales_w_o`, `optional_sales` = `optional_sales` + `opt_sales`
                WHERE `id` = NEW.`package_id`;
		ELSE
			INSERT INTO `package_sales`(`id`,`name`,`sales`,`sales_with_optionals`,`optionals_sales`)
				VALUES (NEW.`package_id`,(SELECT name FROM `package` WHERE `id` = NEW.`package_id`),1,`sales_w_o`,`opt_sales`);
		END IF;
	END IF;
END//

-- when an order is accepted update the sales relative to the package's validity period sold
DROP TRIGGER IF EXISTS `update_period_sales`//
CREATE TRIGGER `update_period_sales` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 1 THEN
		IF EXISTS (SELECT `package_id`,`months` FROM `validty_period_sales` WHERE `package_id` = NEW.`package_id` AND `months` = NEW.`months`) THEN
			UPDATE `validty_period_sales`
				SET `sales` = `sales` + 1
                WHERE `package_id` = NEW.`package_id` AND `months` = NEW.`months`;
		ELSE
			INSERT INTO `validty_period_sales`(`package_id`,`months`,`monthly_fee`,`sales`)
				VALUES (NEW.`package_id`,NEW.`months`,(SELECT `monthly_fee` FROM `validity_period` WHERE `package_id` = NEW.`package_id` AND `months` = NEW.`months`),1);
		END IF;
	END IF;
END//

DELIMITER //

-- when an order is accepted update the sales relative to the optionals sold
DROP TRIGGER IF EXISTS `update_optional_sales`//
CREATE TRIGGER `update_optional_sales` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `opt` varchar(30);
	DECLARE `cur` CURSOR FOR SELECT `optional` FROM `order_to_optional` WHERE `order_id` = NEW.`id`;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET `opt` = '';
	IF NEW.`accepted` = 1 THEN
		IF EXISTS (SELECT `optional` FROM `order_to_optional` WHERE `order_id` = NEW.`id`) THEN
			OPEN cur;
			cur_loop: LOOP
				FETCH `cur` INTO `opt`;
				INSERT INTO `check`(`value`)
					VALUES(`opt`);
			END LOOP;
		END IF;
	END IF;
END//

DELIMITER ;

DELIMITER //

-- when an order state goes from refused to accepted, update the failed_attributes attribute of the user that placed the order and 
-- delete the order from the list of suspended orders
DROP TRIGGER IF EXISTS `order_accepted`//
CREATE TRIGGER `order_accepted` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 1 AND OLD.`accepted` = 0 THEN
		UPDATE `user`
			SET `failed_payments` = `failed_payments` - 1
			WHERE `username` = NEW.`user`;
		DELETE FROM `suspended_orders` WHERE `id` = NEW.`id`;
	END IF;
END//

-- when an order is refused, increase the failed_payments attribute of the user that placed the order and
-- add the order to the list of suspended orders
DROP TRIGGER IF EXISTS `order_refused`//
CREATE TRIGGER `order_refused` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 0 THEN
		UPDATE `user`
			SET `failed_payments` = `failed_payments` + 1
			WHERE `username` = NEW.`user`;
		INSERT INTO `suspended_orders`(`id`,`user`,`total`)
			VALUES(NEW.`id`,NEW.`user`,NEW.`total`);
	END IF;
END//

-- when an order is accepted create the corresponding activation schedule entry
DROP TRIGGER IF EXISTS `create_activation_schedule`//
CREATE TRIGGER `create_activation_schedule` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `opt` varchar(30);
	DECLARE `cur` CURSOR FOR SELECT `optional` FROM `order_to_optional` WHERE `order_id` = NEW.`id`;	
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET `opt` = '';
	IF NEW.`accepted` = 1 THEN
		INSERT INTO `activation_schedule`(`user`,`package_id`,`activation_date`,`deactivation_date`)
			VALUES(NEW.`user`,NEW.`package_id`,NEW.`activation_date`,DATE_ADD(NEW.`activation_date`, INTERVAL NEW.months MONTH));
		OPEN cur;
        cur_loop: LOOP
			FETCH `cur` INTO `opt`;
				INSERT INTO `schedule_to_optional`(`user`,`package_id`,`activation_date`,`optional`)
					VALUES(NEW.`user`,NEW.`package_id`,NEW.`activation_date`,`opt`);
        END LOOP;
	END IF;
END//

-- failed_payments 	0->1 insert user in insolvent users list
-- 					1->0 remove user from insolvent users list
DROP TRIGGER IF EXISTS `update_insolvent_users`//
CREATE TRIGGER `update_insolvent_users` AFTER UPDATE ON `user`
FOR EACH ROW
BEGIN
	IF NEW.`failed_payments` = 1 AND OLD.`failed_payments` = 0 THEN
		INSERT INTO `insolvent_users`(`user`,`email`,`failed_payments`)
			VALUES(NEW.`username`,NEW.`email`,NEW.`failed_payments`);
	ELSE
		IF NEW.`failed_payments` = 0 AND OLD.`failed_payments` = 1 THEN
			DELETE FROM `insolvent_users` WHERE `user` = NEW.`username`;
		END IF;
	END IF;
END//

DELIMITER ;

-- SHOW TRIGGERS;

