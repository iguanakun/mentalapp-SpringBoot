-- monitoring_negative_feels table creation script
-- using mentalapp database

-- Select database
USE `mentalapp`;

-- Temporarily disable foreign key constraints
SET foreign_key_checks = 0;

-- Drop table if it exists
DROP TABLE IF EXISTS `monitoring_negative_feels`;

-- Re-enable foreign key constraints
SET foreign_key_checks = 1;

-- Create monitoring_negative_feels table
CREATE TABLE `monitoring_negative_feels` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `monitoring_id` BIGINT,
  `negative_feel_id` BIGINT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_monitoring_negative_feels_on_monitoring_id` (`monitoring_id`),
  KEY `index_monitoring_negative_feels_on_negative_feel_id` (`negative_feel_id`),
  CONSTRAINT `fk_monitoring_negative_feels_monitoring` FOREIGN KEY (`monitoring_id`) REFERENCES `monitorings` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_monitoring_negative_feels_negative_feel` FOREIGN KEY (`negative_feel_id`) REFERENCES `negative_feels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;