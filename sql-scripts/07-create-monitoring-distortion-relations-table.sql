-- monitoring_distortion_relations table creation script
-- using mentalapp database

-- Select database
USE `mentalapp`;

-- Temporarily disable foreign key constraints
SET foreign_key_checks = 0;

-- Drop table if it exists
DROP TABLE IF EXISTS `monitoring_distortion_relations`;

-- Re-enable foreign key constraints
SET foreign_key_checks = 1;

-- Create monitoring_distortion_relations table
CREATE TABLE `monitoring_distortion_relations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `monitoring_id` BIGINT,
  `distortion_list_id` BIGINT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_monitoring_distortion_relations_on_monitoring_id` (`monitoring_id`),
  KEY `index_monitoring_distortion_relations_on_distortion_list_id` (`distortion_list_id`),
  CONSTRAINT `fk_monitoring_distortion_relations_monitoring` FOREIGN KEY (`monitoring_id`) REFERENCES `monitorings` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_monitoring_distortion_relations_distortion_list` FOREIGN KEY (`distortion_list_id`) REFERENCES `distortion_lists` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;