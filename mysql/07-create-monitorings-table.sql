-- monitorings table creation script
-- using mentalapp database

-- Select database
USE `mentalapp`;

-- Temporarily disable foreign key constraints
SET foreign_key_checks = 0;

-- Drop table if it exists
DROP TABLE IF EXISTS `monitorings`;

-- Re-enable foreign key constraints
SET foreign_key_checks = 1;

-- Create monitorings table
CREATE TABLE `monitorings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `fact` TEXT,
  `mind` TEXT,
  `why_correct` TEXT,
  `why_doubt` TEXT,
  `new_thought` TEXT,
  `user_id` INT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `index_monitorings_on_user_id` (`user_id`),
  CONSTRAINT `fk_monitorings_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Insert test data
INSERT INTO `monitorings` (`fact`, `mind`, `why_correct`, `why_doubt`, `new_thought`, `user_id`, `created_at`, `updated_at`)
VALUES 
('I received criticism during my presentation.', 'Everyone thinks I am incompetent.', 'Some people did look unimpressed.', 'Not everyone was frowning. Some nodded in agreement.', 'Some people may have had concerns, but that does not mean everyone thinks I am incompetent.', 1, NOW(), NOW()),
('I was not invited to a team lunch.', 'They do not like me and excluded me on purpose.', 'I was not included in the group message.', 'They might have assumed I was busy or the message did not reach me.', 'I should check if it was an oversight before assuming negative intentions.', 1, NOW(), NOW()),
('I made a mistake in the financial report.', 'I am going to be fired for this error.', 'It was an important report.', 'Everyone makes mistakes. I fixed it quickly.', 'Making a mistake does not define my overall performance or value to the company.', 2, NOW(), NOW());