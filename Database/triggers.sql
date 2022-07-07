USE `db2_jpa_project`;

DELIMITER //

-- when a package is inserted, create the relative entry in package_sales
DROP TRIGGER IF EXISTS `create_package_sales`//
CREATE TRIGGER `create_package_sales` AFTER INSERT ON `package`
FOR EACH ROW
BEGIN
	INSERT INTO `package_sales`(`id`,`name`,`sales`,`sales_with_optionals`,`optionals_sales`)
		VALUES (NEW.`id`,NEW.`name`,0,0,0);
END//

-- when a new package is inserted, create the relative entry in package_sales
DROP TRIGGER IF EXISTS `create_vp_sales`//
CREATE TRIGGER `create_vp_sales` AFTER INSERT ON `validity_period`
FOR EACH ROW
BEGIN
	INSERT INTO `validity_period_sales`(`package_id`,`months`,`monthly_fee`,`sales`)
		VALUES (NEW.`package_id`,NEW.`months`,NEW.`monthly_fee`,0);
END//

-- when a new package is inserted, create the relative entry in package_sales
DROP TRIGGER IF EXISTS `create_optional_sales`//
CREATE TRIGGER `create_optional_sales` AFTER INSERT ON `optional`
FOR EACH ROW
BEGIN
	INSERT INTO `optional_sales`(`name`,`sales`)
		VALUES (NEW.`name`,0);
END//

-- when an order is updated and becomes accepted, update the relative package sales data
DROP TRIGGER IF EXISTS `update_package_sales1`//
CREATE TRIGGER `update_package_sales1` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `sales_w_o` int;
	DECLARE `opt_sales` int;
    SET `opt_sales` = (SELECT count(*) FROM `order_to_optional` WHERE `order_id` = NEW.`id`);
    SET `sales_w_o` = (SELECT count(DISTINCT(`order_id`)) FROM `order_to_optional` WHERE `order_id` = NEW.`id`);
	IF NEW.`accepted` = 1 AND OLD.`accepted` = 0 THEN
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

-- when an order is inserted and is accepted, update the sales relative to the corresponding package (not the information
-- about the optionals because the bridge table order_to_optional is probably not yet updated)
DROP TRIGGER IF EXISTS `update_package_sales2`//
CREATE TRIGGER `update_package_sales2` AFTER INSERT ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 1 THEN
		IF EXISTS (SELECT * FROM `package_sales` WHERE `id` = NEW.`package_id`) THEN
			UPDATE `package_sales`
				SET `sales` = `sales` + 1
                WHERE `id` = NEW.`package_id`;
		ELSE
			INSERT INTO `package_sales`(`id`,`name`,`sales`,`sales_with_optionals`,`optionals_sales`)
				VALUES (NEW.`package_id`,(SELECT name FROM `package` WHERE `id` = NEW.`package_id`),1,0,0);
		END IF;
	END IF;
END//

-- when new order-optional links are inserted, if the relative order is accepted update the sales relative to the order's package
DROP TRIGGER IF EXISTS `update_package_sales3`//
CREATE TRIGGER `update_package_sales3` AFTER INSERT ON `order_to_optional`
FOR EACH ROW
BEGIN
	DECLARE `pkg_id` int;
    SET `pkg_id` = (SELECT `package_id` FROM `order` WHERE `id` = NEW.`order_id`);    
	IF (SELECT `accepted` FROM `order` WHERE `id` = NEW.`order_id`) = 1 THEN
		IF EXISTS (SELECT * FROM `package_sales` WHERE `id` = `pkg_id`) THEN
			UPDATE `package_sales`
				SET `sales_with_optionals` = (SELECT count(DISTINCT `order_id`) FROM `order_to_optional` WHERE `order_id` IN (SELECT `id` FROM `order` WHERE `package_id` = `pkg_id` AND `accepted` = 1)), `optionals_sales` = `optionals_sales` + 1
                WHERE `id` = `pkg_id`;
		END IF;
	END IF;
END//

-- when an order is updated and is now accepted, update the sales relative to the sold package validity period
DROP TRIGGER IF EXISTS `update_period_sales1`//
CREATE TRIGGER `update_period_sales1` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 1 AND OLD.`accepted` = 0 THEN
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

-- when an accepted order is inserted, update the sales relative to the sold package validity period
DROP TRIGGER IF EXISTS `update_period_sales2`//
CREATE TRIGGER `update_period_sales2` AFTER INSERT ON `order`
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

-- when new order-optional links are inserted, if the relative order is accepted update the optional sales
DROP TRIGGER IF EXISTS `update_optional_sales1`//
CREATE TRIGGER `update_optional_sales1` AFTER INSERT ON `order_to_optional`
FOR EACH ROW
BEGIN
	IF (SELECT `accepted` FROM `order` WHERE `id` = NEW.`order_id`) = 1 THEN
		IF EXISTS (SELECT * FROM `optional_sales` WHERE `name` = NEW.`optional`) THEN
			UPDATE `optional_sales`
				SET `sales` = `sales` + 1
                WHERE `name` = NEW.`optional`;
		ELSE
			INSERT INTO `optional_sales`(`name`,`sales`)
				VALUES (NEW.`optional`,1);
        END IF;
    END IF;
END//

-- when an order is updated and is now accepted, update the sold optionals sales data
DROP TRIGGER IF EXISTS `update_optional_sales2`//
CREATE TRIGGER `update_optional_sales2` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `done` int DEFAULT FALSE;
	DECLARE `opt` varchar(30);
	DECLARE `cur` CURSOR FOR SELECT `optional` FROM `order_to_optional` WHERE `order_id` = NEW.`id`;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET `done` = TRUE;
	IF NEW.`accepted` = 1 AND OLD.`accepted` = 0 THEN
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

-- when a refused order is inserted, add it to the suspended orders table and add the user to the list of insolvent users
DROP TRIGGER IF EXISTS `suspended_orders1`//
CREATE TRIGGER `suspended_orders1` AFTER INSERT ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 0 THEN
		IF NOT EXISTS (SELECT * FROM `suspended_order` WHERE `id` = NEW.`id`) THEN            
			INSERT INTO `suspended_order`(`id`,`user`,`total`)
				VALUES(NEW.`id`,NEW.`user`,NEW.`total`);
		END IF;
		IF NOT EXISTS (SELECT * FROM `insolvent_user` WHERE `user` = NEW.`user`) THEN
			INSERT INTO `insolvent_user`(`user`,`email`)
				VALUES(NEW.`user`,(SELECT `email` FROM `user` WHERE `username` = NEW.`user`));
		END IF;
	END IF;
END//

-- when an order goes from refused to accepted, remove it from suspended orders table and if the user has now zero refused orders 
-- remove it from the list of insolvent users
DROP TRIGGER IF EXISTS `suspended_orders2`//
CREATE TRIGGER `suspended_orders2` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 1 AND OLD.`accepted` = 0 THEN
		DELETE FROM `suspended_order`
			WHERE `id` = NEW.`id`;
		IF (SELECT count(*) FROM `order` WHERE `user` = NEW.`user` AND `accepted` = 0) = 0 THEN
			DELETE FROM `insolvent_user`
				WHERE `user` = NEW.`user`;
        END IF;
	END IF;
END//

-- when an order goes from refused to accepted, create the corresponding activation schedule entry
DROP TRIGGER IF EXISTS `create_activation_schedule1`//
CREATE TRIGGER `create_activation_schedule1` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `done` int DEFAULT FALSE;
	DECLARE `opt` varchar(30);
	DECLARE `cur` CURSOR FOR SELECT `optional` FROM `order_to_optional` WHERE `order_id` = NEW.`id`;	
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET `done` = TRUE;
	IF NEW.`accepted` = 1 AND OLD.`accepted` = 0 THEN
		INSERT INTO `activation_schedule`(`user`,`order_id`,`package_id`,`activation_date`,`deactivation_date`)
			VALUES(NEW.`user`,NEW.`id`,NEW.`package_id`,NEW.`activation_date`,DATE_ADD(NEW.`activation_date`, INTERVAL NEW.`months` MONTH));
		OPEN cur;
        `label1`: LOOP
			FETCH `cur` INTO `opt`;
            IF `done` THEN
				LEAVE `label1`;
			END IF;
			INSERT INTO `schedule_to_optional`(`schedule_id`,`optional`)
				VALUES((SELECT `id` FROM `activation_schedule` WHERE `order_id` = NEW.`id`),`opt`);
        END LOOP `label1`;
	END IF;
END//

-- when an accepted order is inserted create the corresponding activation schedule entry
DROP TRIGGER IF EXISTS `create_activation_schedule2`//
CREATE TRIGGER `create_activation_schedule2` AFTER INSERT ON `order`
FOR EACH ROW
BEGIN
	IF NEW.`accepted` = 1 THEN
		INSERT INTO `activation_schedule`(`user`,`order_id`,`package_id`,`activation_date`,`deactivation_date`)
			VALUES(NEW.`user`,NEW.`id`,NEW.`package_id`,NEW.`activation_date`,DATE_ADD(NEW.`activation_date`, INTERVAL NEW.`months` MONTH));
	END IF;
END//

-- when data is inserted into the order_to_optional table, if the order is accepted update the schedule_to_optional table accordingly
DROP TRIGGER IF EXISTS `create_activation_schedule3`//
CREATE TRIGGER `create_activation_schedule3` AFTER INSERT ON `order_to_optional`
FOR EACH ROW
BEGIN
	IF (SELECT `accepted` FROM `order` WHERE `id` = NEW.`order_id`) = 1 THEN
		INSERT INTO `schedule_to_optional`(`schedule_id`,`optional`)
			VALUES((SELECT `id` FROM `activation_schedule` WHERE `order_id` = NEW.`order_id`),NEW.`optional`);
	END IF;
END//

-- when the number of failed payments in an order is updated, update the user's failed payments attribute and update
-- the auditing table accordingly
DROP TRIGGER IF EXISTS `update_auditing_table1`//
CREATE TRIGGER `update_auditing_table1` AFTER UPDATE ON `order`
FOR EACH ROW
BEGIN
	DECLARE `new_fp` int;
	IF NEW.`failed_payments` > OLD.`failed_payments` THEN
		SET `new_fp` = (SELECT sum(`failed_payments`) FROM `order` WHERE `user` = NEW.`user`);
		UPDATE `user`
			SET `failed_payments` = `new_fp`
            WHERE `username` = NEW.`user`;
		IF `new_fp` >= 3 THEN
			IF NOT EXISTS (SELECT * FROM `auditing_table` WHERE `user` = NEW.`user`) THEN
				INSERT INTO `auditing_table`(`user`,`email`,`amount`,`last_rejection`)
					VALUES(NEW.`user`,(SELECT `email` FROM `user` WHERE `username` = NEW.`user`),NEW.`total`,NEW.`last_rejection`);
			ELSE
				UPDATE `auditing_table`
					SET `amount` = NEW.`total`, `last_rejection` = NEW.`last_rejection`
                    WHERE `user` = NEW.`user`;
			END IF;
		END IF;
	END IF;
END//

-- when a new order is inserted, if it has failed payments associated, update the user's failed payments attribute and update
-- the auditing table accordingly
DROP TRIGGER IF EXISTS `update_auditing_table2`//
CREATE TRIGGER `update_auditing_table2` AFTER INSERT ON `order`
FOR EACH ROW
BEGIN
	DECLARE `new_fp` int;
	IF NEW.`failed_payments` > 0 THEN
		SET `new_fp` = (SELECT sum(`failed_payments`) FROM `order` WHERE `user` = NEW.`user`);
		UPDATE `user`
			SET `failed_payments` = `new_fp`
            WHERE `username` = NEW.`user`;
		IF `new_fp` >= 3 THEN
			IF NOT EXISTS (SELECT * FROM `auditing_table` WHERE `user` = NEW.`user`) THEN
				INSERT INTO `auditing_table`(`user`,`email`,`amount`,`last_rejection`)
					VALUES(NEW.`user`,(SELECT `email` FROM `user` WHERE `username` = NEW.`user`),NEW.`total`,NEW.`last_rejection`);
			ELSE
				UPDATE `auditing_table`
					SET `amount` = NEW.`total`, `last_rejection` = NEW.`last_rejection`
                    WHERE `user` = NEW.`user`;
			END IF;
		END IF;
	END IF;
END//


DELIMITER ;

-- SHOW TRIGGERS;

