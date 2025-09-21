-- cbt_basics_negative_feels と cbt_basics_positive_feels テーブルの作成スクリプト
-- mentalappデータベースを使用

-- データベースの選択
USE `mentalapp`;

-- 外部キー制約を一時的に無効化
SET foreign_key_checks = 0;

-- 既存のテーブルがあれば削除
DROP TABLE IF EXISTS `cbt_basics_negative_feels`;
DROP TABLE IF EXISTS `cbt_basics_positive_feels`;

-- 外部キー制約を再度有効化
SET foreign_key_checks = 1;

-- cbt_basics_negative_feels テーブルの作成
CREATE TABLE `cbt_basics_negative_feels` (
  `cbt_basic_id` int NOT NULL,
  `negative_feel_id` int NOT NULL,
  PRIMARY KEY (`cbt_basic_id`, `negative_feel_id`),
  KEY `index_cbt_basics_negative_feels_on_negative_feel_id` (`negative_feel_id`),
  CONSTRAINT `fk_cbt_basics_negative_feels_cbt_basic` FOREIGN KEY (`cbt_basic_id`) REFERENCES `cbt_basics` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cbt_basics_negative_feels_negative_feel` FOREIGN KEY (`negative_feel_id`) REFERENCES `negative_feels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- cbt_basics_positive_feels テーブルの作成
CREATE TABLE `cbt_basics_positive_feels` (
  `cbt_basic_id` int NOT NULL,
  `positive_feel_id` int NOT NULL,
  PRIMARY KEY (`cbt_basic_id`, `positive_feel_id`),
  KEY `index_cbt_basics_positive_feels_on_positive_feel_id` (`positive_feel_id`),
  CONSTRAINT `fk_cbt_basics_positive_feels_cbt_basic` FOREIGN KEY (`cbt_basic_id`) REFERENCES `cbt_basics` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cbt_basics_positive_feels_positive_feel` FOREIGN KEY (`positive_feel_id`) REFERENCES `positive_feels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;