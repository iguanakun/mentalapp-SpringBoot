-- negative_feels テーブルの作成スクリプト
-- mentalappデータベースを使用

-- データベースの選択
USE `mentalapp`;

-- 外部キー制約を一時的に無効化
SET foreign_key_checks = 0;

-- 既存のテーブルがあれば削除
DROP TABLE IF EXISTS `negative_feels`;

-- 外部キー制約を再度有効化
SET foreign_key_checks = 1;

-- negative_feels テーブルの作成
CREATE TABLE `negative_feels` (
  `id` int NOT NULL AUTO_INCREMENT,
  `negative_feel_name` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- テストデータの挿入
INSERT INTO `negative_feels` (`negative_feel_name`, `created_at`, `updated_at`)
VALUES 
('不安', NOW(), NOW()),
('苦しい', NOW(), NOW()),
('心配', NOW(), NOW()),
('憂うつ', NOW(), NOW()),
('不快感', NOW(), NOW()),
('嫌悪', NOW(), NOW()),
('恥ずかしい', NOW(), NOW()),
('怒り', NOW(), NOW()),
('悲しい', NOW(), NOW()),
('落ち込む', NOW(), NOW()),
('怖い', NOW(), NOW()),
('動揺', NOW(), NOW()),
('疲れている', NOW(), NOW()),
('罪悪感', NOW(), NOW());