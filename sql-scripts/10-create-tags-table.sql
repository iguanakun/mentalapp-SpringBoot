-- tags table creation script
-- using mentalapp database

-- Select database
USE `mentalapp`;

-- Temporarily disable foreign key constraints
SET foreign_key_checks = 0;

-- Drop table if it exists
DROP TABLE IF EXISTS `tags`;

-- Re-enable foreign key constraints
SET foreign_key_checks = 1;

-- Create tags table
CREATE TABLE `tags` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `tag_name` VARCHAR(255) NOT NULL,
  `user_id` BIGINT,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_tags_on_user_id` (`user_id`),
  CONSTRAINT `fk_tags_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;