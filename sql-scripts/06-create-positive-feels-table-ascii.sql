-- positive_feels table creation script
-- using mentalapp database

-- Select database
USE `mentalapp`;

-- Temporarily disable foreign key constraints
SET foreign_key_checks = 0;

-- Drop table if it exists
DROP TABLE IF EXISTS `positive_feels`;

-- Re-enable foreign key constraints
SET foreign_key_checks = 1;

-- Create positive_feels table
CREATE TABLE `positive_feels` (
  `id` int NOT NULL AUTO_INCREMENT,
  `positive_feel_name` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Insert test data
INSERT INTO `positive_feels` (`positive_feel_name`, `created_at`, `updated_at`)
VALUES 
('Loved', NOW(), NOW()),
('Enthusiastic', NOW(), NOW()),
('Free', NOW(), NOW()),
('Joy', NOW(), NOW()),
('Calm', NOW(), NOW()),
('Fulfilled', NOW(), NOW()),
('Curious', NOW(), NOW()),
('Excited', NOW(), NOW()),
('Confident', NOW(), NOW()),
('Relieved', NOW(), NOW()),
('Motivated', NOW(), NOW()),
('Grateful', NOW(), NOW()),
('Compassionate', NOW(), NOW()),
('Impressed', NOW(), NOW());