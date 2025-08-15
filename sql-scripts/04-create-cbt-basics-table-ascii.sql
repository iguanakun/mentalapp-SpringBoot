-- cbt_basics table creation script
-- using mentalapp database

-- Select database
USE `mentalapp`;

-- Temporarily disable foreign key constraints
SET foreign_key_checks = 0;

-- Drop table if it exists
DROP TABLE IF EXISTS `cbt_basics`;

-- Re-enable foreign key constraints
SET foreign_key_checks = 1;

-- Create cbt_basics table
CREATE TABLE `cbt_basics` (
  `id` int NOT NULL AUTO_INCREMENT,
  `fact` text,
  `mind` text,
  `body` text,
  `behavior` text,
  `user_id` int NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_cbt_basics_on_user_id` (`user_id`),
  CONSTRAINT `fk_cbt_basics_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Insert test data
INSERT INTO `cbt_basics` (`fact`, `mind`, `body`, `behavior`, `user_id`, `created_at`, `updated_at`)
VALUES 
('I had an important presentation today.', 'I think I did not do well. Everyone looked bored.', 'Stomach pain, shaking hands', 'Returned to my seat immediately after the presentation, avoiding questions', 1, NOW(), NOW()),
('I declined an invitation from a friend.', 'They can have fun without me. I do not want to be a burden.', 'Fatigue, stiff shoulders', 'Stayed home alone, kept checking social media', 1, NOW(), NOW()),
('I was selected as the leader for a new project.', 'I cannot do this. I will fail and disappoint everyone.', 'Shortness of breath, palpitations', 'Consulted excessively with other team members, postponed decisions', 2, NOW(), NOW()),
('A colleague criticized my proposal in a meeting.', 'My ideas have no value. I should have studied more.', 'Face getting hot, headache', 'Apologized after the meeting and withdrew the proposal', 2, NOW(), NOW());