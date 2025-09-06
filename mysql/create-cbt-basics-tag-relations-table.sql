-- CBT BasicsとTagの中間テーブル
CREATE TABLE IF NOT EXISTS `cbt_basics_tag_relations` (
  `cbt_basic_id` INT NOT NULL,
  `tag_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`cbt_basic_id`, `tag_id`),
  FOREIGN KEY (`cbt_basic_id`) REFERENCES `cbt_basics` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;