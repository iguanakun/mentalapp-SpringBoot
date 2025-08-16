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
('不安', NOW(), NOW()),
('痛み', NOW(), NOW()),
('心配', NOW(), NOW()),
('抑うつ', NOW(), NOW()),
('不快感', NOW(), NOW()),
('嫌悪感', NOW(), NOW()),
('恥ずかしさ', NOW(), NOW()),
('怒り', NOW(), NOW()),
('悲しみ', NOW(), NOW()),
('落ち込み', NOW(), NOW()),
('恐れ', NOW(), NOW()),
('イライラ', NOW(), NOW()),
('疲れ', NOW(), NOW()),
('罪悪感', NOW(), NOW());