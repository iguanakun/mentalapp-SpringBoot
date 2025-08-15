-- negative_feels table creation script
-- using mentalapp database

-- Select database
USE `mentalapp`;

-- Temporarily disable foreign key constraints
SET foreign_key_checks = 0;

-- Drop table if it exists
DROP TABLE IF EXISTS `negative_feels`;

-- Re-enable foreign key constraints
SET foreign_key_checks = 1;

-- Create negative_feels table
CREATE TABLE `negative_feels` (
  `id` int NOT NULL AUTO_INCREMENT,
  `negative_feel_name` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Insert test data
INSERT INTO `negative_feels` (`negative_feel_name`, `created_at`, `updated_at`)
VALUES 
('Anxiety', NOW(), NOW()),
('Pain', NOW(), NOW()),
('Worry', NOW(), NOW()),
('Depression', NOW(), NOW()),
('Discomfort', NOW(), NOW()),
('Disgust', NOW(), NOW()),
('Embarrassment', NOW(), NOW()),
('Anger', NOW(), NOW()),
('Sadness', NOW(), NOW()),
('Feeling down', NOW(), NOW()),
('Fear', NOW(), NOW()),
('Agitation', NOW(), NOW()),
('Tiredness', NOW(), NOW()),
('Guilt', NOW(), NOW());