-- 認知再構成法とタグの中間テーブル
CREATE TABLE IF NOT EXISTS `cbt_cr_tag_relations` (
  `cbt_cr_id` BIGINT NOT NULL,
  `tag_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`cbt_cr_id`, `tag_id`),
  FOREIGN KEY (`cbt_cr_id`) REFERENCES `cbt_cr` (`id`) ON DELETE CASCADE,
  FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;