-- cbt_basics テーブルの作成スクリプト
-- mentalappデータベースを使用

-- データベースの選択
USE `mentalapp`;

-- 外部キー制約を一時的に無効化
SET foreign_key_checks = 0;

-- 既存のテーブルがあれば削除
DROP TABLE IF EXISTS `cbt_basics`;

-- 外部キー制約を再度有効化
SET foreign_key_checks = 1;

-- cbt_basics テーブルの作成
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

-- テストデータの挿入
INSERT INTO `cbt_basics` (`fact`, `mind`, `body`, `behavior`, `user_id`, `created_at`, `updated_at`)
VALUES 
('今日、重要なプレゼンテーションがありました。', '上手くできなかったと思う。みんなは退屈そうだった。', '胃が痛い、手が震える', 'プレゼン後すぐに席に戻り、質問を避けた', 1, NOW(), NOW()),
('友人からの招待を断りました。', '私がいなくても楽しめるだろう。迷惑をかけたくない。', '疲労感、肩こり', '家に一人でいて、SNSをチェックし続けた', 1, NOW(), NOW()),
('新しいプロジェクトのリーダーに選ばれました。', '私には無理だ。失敗して皆を失望させるだろう。', '息苦しさ、動悸', '他のチームメンバーに過度に相談し、決断を先延ばしにした', 2, NOW(), NOW()),
('同僚が私の提案を会議で批判しました。', '私の考えは価値がない。もっと勉強すべきだった。', '顔が熱くなる、頭痛', '会議後に謝罪し、提案を撤回した', 2, NOW(), NOW());