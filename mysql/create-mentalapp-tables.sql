-- mentalappデータベースのテーブル作成スクリプト
-- entityパッケージのクラスに基づいて作成

-- データベースの選択
USE `mentalapp`;

-- 外部キー制約を一時的に無効化
SET foreign_key_checks = 0;

-- 既存のテーブルがあれば削除
DROP TABLE IF EXISTS `users_roles`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `role`;

-- 外部キー制約を再度有効化
SET foreign_key_checks = 1;

--
-- Table structure for table `user`
--
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` char(80) NOT NULL,
  `enabled` tinyint NOT NULL,  
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) NOT NULL,
  `email` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `role`
--
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

--
-- Table structure for table `users_roles` (中間テーブル)
--
CREATE TABLE `users_roles` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  
  PRIMARY KEY (`user_id`,`role_id`),
  
  KEY `FK_ROLE_idx` (`role_id`),
  
  CONSTRAINT `FK_USER_ID` FOREIGN KEY (`user_id`) 
  REFERENCES `user` (`id`) 
  ON DELETE CASCADE ON UPDATE NO ACTION,
  
  CONSTRAINT `FK_ROLE_ID` FOREIGN KEY (`role_id`) 
  REFERENCES `role` (`id`) 
  ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- サンプルデータの挿入
--

-- ロールデータ
INSERT INTO `role` (name)
VALUES 
('ROLE_EMPLOYEE'),
('ROLE_MANAGER'),
('ROLE_ADMIN');

-- ユーザーデータ (パスワードはBCryptでハッシュ化: 'password')
INSERT INTO `user` (`username`,`password`,`enabled`, `first_name`, `last_name`, `email`)
VALUES 
('admin','$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q',1,'Admin', 'User', 'admin@example.com'),
('user','$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q',1,'Normal', 'User', 'user@example.com');

-- ユーザーとロールの関連付け
INSERT INTO `users_roles` (user_id, role_id)
VALUES 
(1, 1),
(1, 2),
(1, 3),
(2, 1);