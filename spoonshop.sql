CREATE DATABASE spoonshop CHARSET utf8;
USE spoonshop;
CREATE TABLE `tb_cart` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20) DEFAULT NULL,
  `item_id` BIGINT(20) DEFAULT NULL,
  `item_title` VARCHAR(100) DEFAULT NULL,
  `item_image` VARCHAR(200) DEFAULT NULL,
  `item_price` BIGINT(20) DEFAULT NULL COMMENT '单位：分',
  `num` INT(10) DEFAULT NULL,
  `created` DATETIME DEFAULT NULL,
  `updated` DATETIME DEFAULT NULL,
  PRIMARY KEY  (`id`),
  KEY `AK_user_itemId` (`user_id`,`item_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;
INSERT  INTO `tb_cart`(`id`,`user_id`,`item_id`,`item_title`,`item_image`,`item_price`,`num`,`created`,`updated`) VALUES (22,7,562378,'三星 W999 黑色 电信3G手机 双卡双待双通','http://image.taotao.com/jd/d2ac340e728d4c6181e763e772a9944a.jpg',4299000,3,'2018-02-09 10:50:52','2018-11-14 13:44:04'),(23,7,562379,'三星 W999 黑色 电信3G手机 双卡双待双通','http://image.jt.com/2014/10/23/2014102305423212301343.jpg',4299000,11,'2018-10-23 11:42:03','2018-11-14 13:44:37');