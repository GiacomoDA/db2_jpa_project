USE `db_telco_project`;

DELIMITER //

-- when an order is accepted update the sales relative to the sold package
DROP TRIGGER IF EXISTS `update_package_sales`//
CREATE TRIGGER `update_package_sales` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `sales_w_o` int;
	DECLARE `opt_sales` int;
    SET `opt_sales` = (SELECT count(*) FROM `order_to_optional` WHERE `order_id` = NEW.`id`);
    SET `sales_w_o` = (SELECT count(DISTINCT(`order_id`)) FROM `order_to_optional` WHERE `order_id` = NEW.`id`);
	IF NEW.`accepted` = 1 THEN
		IF EXISTS (SELECT * FROM `package_sales` WHERE `id` = NEW.`package_id`) THEN
			UPDATE `package_sales`
				SET `sales` = `sales` + 1, `sales_with_optionals` = `sales_with_optionals` + `sales_w_o`, `optionals_sales` = `optionals_sales` + `opt_sales`
                WHERE `id` = NEW.`package_id`;
		ELSE
			INSERT INTO `package_sales`(`id`,`name`,`sales`,`sales_with_optionals`,`optionals_sales`)
				VALUES (NEW.`package_id`,(SELECT name FROM `package` WHERE `id` = NEW.`package_id`),1,`sales_w_o`,`opt_sales`);
		END IF;
	END IF;
END//

-- when an order is accepted update the sales relative to the sold package's validity period
DROP TRIGGER IF EXISTS `update_period_sales`//
CREATE TRIGGER `update_period_sales` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 1 THEN
		IF EXISTS (SELECT * FROM `validity_period_sales` WHERE `package_id` = NEW.`package_id` AND `months` = NEW.`months`) THEN
			UPDATE `validity_period_sales`
				SET `sales` = `sales` + 1
                WHERE `package_id` = NEW.`package_id` AND `months` = NEW.`months`;
		ELSE
			INSERT INTO `validity_period_sales`(`package_id`,`months`,`monthly_fee`,`sales`)
				VALUES (NEW.`package_id`,NEW.`months`,(SELECT `monthly_fee` FROM `validity_period` WHERE `package_id` = NEW.`package_id` AND `months` = NEW.`months`),1);
		END IF;
	END IF;
END//

-- when an order is accepted update the sales relative to the sold optionals
DROP TRIGGER IF EXISTS `update_optional_sales`//
CREATE TRIGGER `update_optional_sales` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `done` int DEFAULT FALSE;
	DECLARE `opt` varchar(30);
	DECLARE `cur` CURSOR FOR SELECT `optional` FROM `order_to_optional` WHERE `order_id` = NEW.`id`;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET `done` = TRUE;
	IF NEW.`accepted` = 1 THEN
		IF EXISTS (SELECT * FROM `order_to_optional` WHERE `order_id` = NEW.`id`) THEN
			OPEN `cur`;
			`label1`: LOOP
				FETCH `cur` INTO `opt`;
                IF `done` THEN
					LEAVE `label1`;
                END IF;
				IF EXISTS (SELECT * FROM `optional_sales` WHERE `name` = `opt`) THEN
					UPDATE `optional_sales`
						SET `sales` = `sales` + 1
						WHERE `name` = `opt`;
				ELSE
					INSERT INTO `optional_sales`(`name`,`sales`)
						VALUES (`opt`,1);
				END IF;
			END LOOP `label1`;
		END IF;
	END IF;
END//

-- when an order state goes from refused to accepted, update the failed_payments attribute of the user that placed the order and 
-- delete the order from the list of suspended orders
DROP TRIGGER IF EXISTS `order_accepted`//
CREATE TRIGGER `order_accepted` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 1 AND OLD.`accepted` = 0 THEN
		UPDATE `user`
			SET `failed_payments` = `failed_payments` - 1
			WHERE `username` = NEW.`user`;
		DELETE FROM `suspended_order` WHERE `id` = NEW.`id`;
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
		IF NOT EXISTS (SELECT * FROM `suspended_order` WHERE `id` = NEW.`id`) THEN            
			INSERT INTO `suspended_order`(`id`,`user`,`total`)
				VALUES(NEW.`id`,NEW.`user`,NEW.`total`);
		END IF;
		IF (SELECT `failed_payments` FROM `user` WHERE `username` = NEW.`user`) >= 3 THEN
			IF EXISTS (SELECT * FROM `auditing_table` WHERE `user` = NEW.`user`) THEN
				UPDATE `auditing_table`
					SET `amount` = NEW.`total`, `last_rejection` = CURRENT_TIMESTAMP()
                    WHERE `user` = NEW.`user`;
			ELSE
				INSERT INTO `auditing_table`(`user`,`email`,`amount`,`last_rejection`)
				VALUES (NEW.`user`,(SELECT `email` FROM `user` WHERE `username` = NEW.`user`),NEW.`total`,CURRENT_TIMESTAMP());            
			END IF;
        END IF;
	END IF;
END//

-- when an order is accepted create the corresponding activation schedule entry
DROP TRIGGER IF EXISTS `create_activation_schedule`//
CREATE TRIGGER `create_activation_schedule` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `done` int DEFAULT FALSE;
	DECLARE `opt` varchar(30);
	DECLARE `cur` CURSOR FOR SELECT `optional` FROM `order_to_optional` WHERE `order_id` = NEW.`id`;	
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET `done` = TRUE;
	IF NEW.`accepted` = 1 THEN
		INSERT INTO `activation_schedule`(`user`,`package_id`,`activation_date`,`deactivation_date`)
			VALUES(NEW.`user`,NEW.`package_id`,NEW.`activation_date`,DATE_ADD(NEW.`activation_date`, INTERVAL NEW.months MONTH));
		OPEN cur;
        `label1`: LOOP
			FETCH `cur` INTO `opt`;
            IF `done` THEN
				LEAVE `label1`;
			END IF;
			INSERT INTO `schedule_to_optional`(`user`,`package_id`,`activation_date`,`optional`)
				VALUES(NEW.`user`,NEW.`package_id`,NEW.`activation_date`,`opt`);
        END LOOP `label1`;
	END IF;
END//

-- depending on the old and the new values of the attribute failed_payments in the user table
-- update the insolvent_user table accordingly

/* OLD | NEW | do
   =0  | =0  | nothing
   >=1 | =0  | remove
   =0  | >=1 | insert
   >=1 | >=1 | update */
   
DROP TRIGGER IF EXISTS `update_insolvent_user`//
CREATE TRIGGER `update_insolvent_user` AFTER UPDATE ON `user`
FOR EACH ROW
BEGIN
	IF NEW.`failed_payments` = 0 AND OLD.`failed_payments` >= 1 THEN
		DELETE FROM `insolvent_user` WHERE `user` = NEW.`username`;
	ELSE
		IF NEW.`failed_payments` >= 1 AND OLD.`failed_payments` = 0 THEN
			INSERT INTO `insolvent_user`(`user`,`email`,`failed_payments`)
				VALUES(NEW.`username`,NEW.`email`,NEW.`failed_payments`);
		ELSE
			IF NEW.`failed_payments` <> 0 THEN
				UPDATE `insolvent_user`
					SET `failed_payments` = NEW.`failed_payments`
					WHERE `user` = NEW.`username`;
			END IF;
		END IF;
	END IF;
END//

DELIMITER ;

-- SHOW TRIGGERS;

