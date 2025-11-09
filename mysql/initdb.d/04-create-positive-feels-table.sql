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
('愛情', NOW(), NOW()),
('熱意', NOW(), NOW()),
('自由', NOW(), NOW()),
('喜び', NOW(), NOW()),
('落ち着いている', NOW(), NOW()),
('充実感', NOW(), NOW()),
('好奇心', NOW(), NOW()),
('ワクワク', NOW(), NOW()),
('自信がある', NOW(), NOW()),
('安心', NOW(), NOW()),
('やる気がある', NOW(), NOW()),
('感謝', NOW(), NOW()),
('思いやり', NOW(), NOW()),
('感動', NOW(), NOW());