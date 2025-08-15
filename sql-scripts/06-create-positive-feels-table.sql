-- positive_feels テーブルの作成スクリプト
-- mentalappデータベースを使用

-- データベースの選択
USE `mentalapp`;

-- 外部キー制約を一時的に無効化
SET foreign_key_checks = 0;

-- 既存のテーブルがあれば削除
DROP TABLE IF EXISTS `positive_feels`;

-- 外部キー制約を再度有効化
SET foreign_key_checks = 1;

-- positive_feels テーブルの作成
CREATE TABLE `positive_feels` (
  `id` int NOT NULL AUTO_INCREMENT,
  `positive_feel_name` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- テストデータの挿入
INSERT INTO `positive_feels` (`positive_feel_name`, `created_at`, `updated_at`)
VALUES 
('愛されている', NOW(), NOW()),
('熱心', NOW(), NOW()),
('自由', NOW(), NOW()),
('喜び', NOW(), NOW()),
('落ち着き', NOW(), NOW()),
('充足感', NOW(), NOW()),
('好奇心', NOW(), NOW()),
('高揚感', NOW(), NOW()),
('自信', NOW(), NOW()),
('ホッとする', NOW(), NOW()),
('やる気', NOW(), NOW()),
('感謝', NOW(), NOW()),
('思いやり', NOW(), NOW()),
('感心', NOW(), NOW());