-- MentalApp SQLite初期化スクリプト

-- userテーブル
CREATE TABLE IF NOT EXISTS user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password CHAR(68) NOT NULL,
    enabled TINYINT NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL
);

-- roleテーブル
CREATE TABLE IF NOT EXISTS role (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL
);

-- users_rolesテーブル
CREATE TABLE IF NOT EXISTS users_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- tagsテーブル
CREATE TABLE IF NOT EXISTS tags (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL
);

-- cbt_basicsテーブル
CREATE TABLE IF NOT EXISTS cbt_basics (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    situation TEXT,
    emotion TEXT,
    thought TEXT,
    evidence TEXT,
    reframe TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- cbt_crテーブル
CREATE TABLE IF NOT EXISTS cbt_cr (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    situation TEXT,
    emotion TEXT,
    thought TEXT,
    cognitive_distortion TEXT,
    evidence_for TEXT,
    evidence_against TEXT,
    balanced_thought TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- 初期データ挿入
INSERT OR IGNORE INTO role (name) VALUES ('ROLE_EMPLOYEE');
INSERT OR IGNORE INTO role (name) VALUES ('ROLE_MANAGER');
INSERT OR IGNORE INTO role (name) VALUES ('ROLE_ADMIN');

INSERT OR IGNORE INTO user (username, password, enabled, first_name, last_name, email) 
VALUES ('admin', '$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1, 'Admin', 'User', 'admin@example.com');

INSERT OR IGNORE INTO user (username, password, enabled, first_name, last_name, email) 
VALUES ('user', '$2a$10$qeS0HEh7urweMojsnwNAR.vcXJeXR1UcMRZ2WcGQl9YeuspUdgF.q', 1, 'Test', 'User', 'user@example.com');

INSERT OR IGNORE INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT OR IGNORE INTO users_roles (user_id, role_id) VALUES (1, 2);
INSERT OR IGNORE INTO users_roles (user_id, role_id) VALUES (1, 3);
INSERT OR IGNORE INTO users_roles (user_id, role_id) VALUES (2, 1);

-- タグサンプルデータ
INSERT OR IGNORE INTO tags (name) VALUES ('不安');
INSERT OR IGNORE INTO tags (name) VALUES ('うつ');
INSERT OR IGNORE INTO tags (name) VALUES ('ストレス');
INSERT OR IGNORE INTO tags (name) VALUES ('仕事');
INSERT OR IGNORE INTO tags (name) VALUES ('人間関係');

-- CBT基本サンプルデータ
INSERT OR IGNORE INTO cbt_basics (user_id, situation, emotion, thought, evidence, reframe) 
VALUES (2, 'プレゼンテーション前', '不安', '失敗するかもしれない', '準備をしっかりした', '準備したので大丈夫');

-- CBT認知再構成サンプルデータ
INSERT OR IGNORE INTO cbt_cr (user_id, situation, emotion, thought, cognitive_distortion, evidence_for, evidence_against, balanced_thought) 
VALUES (2, '上司からの指摘', '落ち込み', '自分はダメな人間だ', '全か無か思考', '今回ミスをした', '普段は評価されている', '今回はミスしたが、普段は良い仕事をしている');