-- 認知の歪みリストテーブル作成スクリプト
-- mentalappデータベースを使用

-- データベースの選択
USE `mentalapp`;

-- 一時的に外部キー制約を無効化
SET foreign_key_checks = 0;

-- テーブルが存在する場合は削除
DROP TABLE IF EXISTS `distortion_lists`;

-- 外部キー制約を再度有効化
SET foreign_key_checks = 1;

-- 認知の歪みリストテーブルの作成
CREATE TABLE `distortion_lists` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `distortion_name` VARCHAR(255) NOT NULL,
  `info` VARCHAR(255),
  `created_at` DATETIME NOT NULL,
  `updated_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- サンプルデータの挿入
INSERT INTO `distortion_lists` (`distortion_name`, `info`, `created_at`, `updated_at`)
VALUES 
('All-or-Nothing Thinking', 'Seeing things in black-and-white categories', NOW(), NOW()),
('Overgeneralization', 'Viewing a negative event as a never-ending pattern of defeat', NOW(), NOW()),
('Mental Filter', 'Picking out a single negative detail and dwelling on it', NOW(), NOW()),
('Disqualifying the Positive', 'Rejecting positive experiences by insisting they "don\'t count"', NOW(), NOW()),
('Jumping to Conclusions', 'Making negative interpretations without definite facts', NOW(), NOW()),
('Magnification or Minimization', 'Exaggerating the importance of problems or minimizing positive qualities', NOW(), NOW()),
('Emotional Reasoning', 'Assuming that negative emotions reflect the way things really are', NOW(), NOW()),
('Should Statements', 'Using "should," "must," or "ought to" statements to motivate yourself', NOW(), NOW()),
('Labeling', 'Attaching a negative label to yourself or others instead of describing behavior', NOW(), NOW()),
('Personalization', 'Seeing yourself as the cause of some negative external event', NOW(), NOW());