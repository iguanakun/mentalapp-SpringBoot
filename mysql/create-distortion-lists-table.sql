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
('白黒思考', '「完璧にやろうとしていない？」', NOW(), NOW()),
('過度な一般化', '「一度の失敗で、また同じ事が起こると思っていない？」', NOW(), NOW()),
('心のフィルター', '「悪い点だけ目についていない？」', NOW(), NOW()),
('マイナス化思考', '「自分を素直に褒めることができない？」', NOW(), NOW()),
('読心術', '「相手のことを思い込んでいない？」', NOW(), NOW()),
('運命の先読み', '「私は一生不幸だ、と思いすぎていない？」', NOW(), NOW()),
('破滅化', '「すこしの失敗を大げさにとらえすぎていない？」', NOW(), NOW()),
('感情的決めつけ', '「感情に巻き込まれていない？」', NOW(), NOW()),
('べき思考', '「自分や相手に厳しすぎない？」', NOW(), NOW()),
('レッテル貼り', '「私はこういう人間だ、と思いすぎていない？」', NOW(), NOW()),
('内なる批評家', '「自分を責めすぎていない？」', NOW(), NOW()),
('他人批判', '「あいつのせい、って思いすぎていない？」', NOW(), NOW());