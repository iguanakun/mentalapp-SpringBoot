-- monitoring_positive_feels table creation script
-- using mentalapp database

-- Select database
USE `mentalapp`;

-- Temporarily disable foreign key constraints
SET foreign_key_checks = 0;

-- Drop table if it exists
DROP TABLE IF EXISTS `monitoring_positive_feels`;

-- Re-enable foreign key constraints
SET foreign_key_checks = 1;

-- Create monitoring_positive_feels table
CREATE TABLE `monitoring_positive_feels` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `monitoring_id` BIGINT,
  `positive_feel_id` BIGINT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_monitoring_positive_feels_on_monitoring_id` (`monitoring_id`),
  KEY `index_monitoring_positive_feels_on_positive_feel_id` (`positive_feel_id`),
  CONSTRAINT `fk_monitoring_positive_feels_monitoring` FOREIGN KEY (`monitoring_id`) REFERENCES `monitorings` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_monitoring_positive_feels_positive_feel` FOREIGN KEY (`positive_feel_id`) REFERENCES `positive_feels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;