--liquibase formatted sql
	
	--changeset sabarinath_subramani: 3
	DROP TABLE IF EXISTS `orders`;
	
	CREATE TABLE `orders` (
	  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
	  `orderid` int(16) unsigned NOT NULL DEFAULT '',
	  `order_status` varchar(256) DEFAULT '',
	  `financial_status` varchar(256) DEFAULT '',
	  `fulfillment_status` varchar(256) DEFAULT '',
	  `total_price` double(10) DEFAULT 0,
	  PRIMARY KEY (`id`),
	  UNIQUE KEY `orderid_unique` (`orderid`)
	) ENGINE=InnoDB DEFAULT CHARSET=latin1;
	
-- rollback drop table orders;