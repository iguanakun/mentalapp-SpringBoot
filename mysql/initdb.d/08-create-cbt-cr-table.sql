-- 認知再構成法テーブル作成スクリプト
-- mentalappデータベースを使用

-- データベースの選択
USE `mentalapp`;

-- 一時的に外部キー制約を無効化
SET foreign_key_checks = 0;

-- テーブルが存在する場合は削除
DROP TABLE IF EXISTS `cbt_cr_negative_feels`;
DROP TABLE IF EXISTS `cbt_cr_positive_feels`;
DROP TABLE IF EXISTS `cbt_cr_distortion_relations`;
DROP TABLE IF EXISTS `cbt_cr`;

-- 外部キー制約を再度有効化
SET foreign_key_checks = 1;

-- 認知再構成法テーブルの作成
CREATE TABLE `cbt_cr` (
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
  CONSTRAINT `fk_cbt_cr_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 認知再構成法とネガティブ感情の中間テーブルの作成
CREATE TABLE `cbt_cr_negative_feels` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cbt_cr_id` BIGINT NOT NULL,
  `negative_feel_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_cbt_cr_negative_feels_cbt_cr` FOREIGN KEY (`cbt_cr_id`) REFERENCES `cbt_cr` (`id`),
  CONSTRAINT `fk_cbt_cr_negative_feels_negative_feel` FOREIGN KEY (`negative_feel_id`) REFERENCES `negative_feels` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 認知再構成法とポジティブ感情の中間テーブルの作成
CREATE TABLE `cbt_cr_positive_feels` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cbt_cr_id` BIGINT NOT NULL,
  `positive_feel_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_cbt_cr_positive_feels_cbt_cr` FOREIGN KEY (`cbt_cr_id`) REFERENCES `cbt_cr` (`id`),
  CONSTRAINT `fk_cbt_cr_positive_feels_positive_feel` FOREIGN KEY (`positive_feel_id`) REFERENCES `positive_feels` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 認知再構成法と思考の歪みの中間テーブルの作成
CREATE TABLE `cbt_cr_distortion_relations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cbt_cr_id` BIGINT NOT NULL,
  `distortion_list_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_cbt_cr_distortion_relations_cbt_cr` FOREIGN KEY (`cbt_cr_id`) REFERENCES `cbt_cr` (`id`),
  CONSTRAINT `fk_cbt_cr_distortion_relations_distortion_list` FOREIGN KEY (`distortion_list_id`) REFERENCES `distortion_lists` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
