-- SQLite Table Creation Script

-- =====================================================
-- User and Role Related Tables
-- =====================================================

-- user table
CREATE TABLE IF NOT EXISTS user (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password CHAR(68) NOT NULL,
    enabled TINYINT NOT NULL,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    email VARCHAR(64) NOT NULL
);

-- role table
CREATE TABLE IF NOT EXISTS role (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name VARCHAR(50) NOT NULL
);

-- users_roles table
CREATE TABLE IF NOT EXISTS users_roles (
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

-- =====================================================
-- Emotion Master Tables
-- =====================================================

-- negative_feels table
CREATE TABLE IF NOT EXISTS negative_feels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    negative_feel_name TEXT NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

-- positive_feels table
CREATE TABLE IF NOT EXISTS positive_feels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    positive_feel_name TEXT NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

-- =====================================================
-- Cognitive Distortion Master Table
-- =====================================================

-- distortion_lists table
CREATE TABLE IF NOT EXISTS distortion_lists (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    distortion_name TEXT NOT NULL,
    info TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL
);

-- =====================================================
-- Tag Table
-- =====================================================

-- tags table
CREATE TABLE IF NOT EXISTS tags (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    tag_name TEXT NOT NULL,
    user_id INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- =====================================================
-- CBT Basic Monitoring Related Tables
-- =====================================================

-- cbt_basics table
CREATE TABLE IF NOT EXISTS cbt_basics (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fact TEXT,
    mind TEXT,
    body TEXT,
    behavior TEXT,
    user_id INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE
);

-- cbt_basics_negative_feels table (junction table)
CREATE TABLE IF NOT EXISTS cbt_basics_negative_feels (
    cbt_basic_id INTEGER NOT NULL,
    negative_feel_id INTEGER NOT NULL,
    PRIMARY KEY (cbt_basic_id, negative_feel_id),
    FOREIGN KEY (cbt_basic_id) REFERENCES cbt_basics(id) ON DELETE CASCADE,
    FOREIGN KEY (negative_feel_id) REFERENCES negative_feels(id) ON DELETE CASCADE
);

-- cbt_basics_positive_feels table (junction table)
CREATE TABLE IF NOT EXISTS cbt_basics_positive_feels (
    cbt_basic_id INTEGER NOT NULL,
    positive_feel_id INTEGER NOT NULL,
    PRIMARY KEY (cbt_basic_id, positive_feel_id),
    FOREIGN KEY (cbt_basic_id) REFERENCES cbt_basics(id) ON DELETE CASCADE,
    FOREIGN KEY (positive_feel_id) REFERENCES positive_feels(id) ON DELETE CASCADE
);

-- cbt_basics_tag_relations table (junction table)
CREATE TABLE IF NOT EXISTS cbt_basics_tag_relations (
    cbt_basic_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    PRIMARY KEY (cbt_basic_id, tag_id),
    FOREIGN KEY (cbt_basic_id) REFERENCES cbt_basics(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- =====================================================
-- CBT Cognitive Restructuring Related Tables
-- =====================================================

-- cbt_cr table
CREATE TABLE IF NOT EXISTS cbt_cr (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fact TEXT,
    mind TEXT,
    why_correct TEXT,
    why_doubt TEXT,
    new_thought TEXT,
    user_id INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

-- cbt_cr_negative_feels table (junction table)
CREATE TABLE IF NOT EXISTS cbt_cr_negative_feels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cbt_cr_id INTEGER NOT NULL,
    negative_feel_id INTEGER NOT NULL,
    FOREIGN KEY (cbt_cr_id) REFERENCES cbt_cr(id),
    FOREIGN KEY (negative_feel_id) REFERENCES negative_feels(id)
);

-- cbt_cr_positive_feels table (junction table)
CREATE TABLE IF NOT EXISTS cbt_cr_positive_feels (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cbt_cr_id INTEGER NOT NULL,
    positive_feel_id INTEGER NOT NULL,
    FOREIGN KEY (cbt_cr_id) REFERENCES cbt_cr(id),
    FOREIGN KEY (positive_feel_id) REFERENCES positive_feels(id)
);

-- cbt_cr_distortion_relations table (junction table)
CREATE TABLE IF NOT EXISTS cbt_cr_distortion_relations (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    cbt_cr_id INTEGER NOT NULL,
    distortion_list_id INTEGER NOT NULL,
    FOREIGN KEY (cbt_cr_id) REFERENCES cbt_cr(id),
    FOREIGN KEY (distortion_list_id) REFERENCES distortion_lists(id)
);

-- cbt_cr_tag_relations table (junction table)
CREATE TABLE IF NOT EXISTS cbt_cr_tag_relations (
    cbt_cr_id INTEGER NOT NULL,
    tag_id INTEGER NOT NULL,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    PRIMARY KEY (cbt_cr_id, tag_id),
    FOREIGN KEY (cbt_cr_id) REFERENCES cbt_cr(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- =====================================================
-- Initial Data Insertion
-- =====================================================

-- Initial data for user table (password: mentalapp)
INSERT OR IGNORE INTO user (username, password, enabled, first_name, last_name, email)
VALUES ('mentalapp', '$2a$10$7eGHQqZ1JXzMZvF8xLqJNO7fZjRXKxYZH8qCL8F5JVF7vH4jXQYXa', 1, 'Mental', 'App', 'mentalapp@example.com');

-- Initial data for negative_feels table
INSERT OR IGNORE INTO negative_feels (negative_feel_name, created_at, updated_at)
VALUES
('不安', datetime('now', 'localtime'), datetime('now', 'localtime')),
('痛み', datetime('now', 'localtime'), datetime('now', 'localtime')),
('心配', datetime('now', 'localtime'), datetime('now', 'localtime')),
('抑うつ', datetime('now', 'localtime'), datetime('now', 'localtime')),
('不快感', datetime('now', 'localtime'), datetime('now', 'localtime')),
('嫌悪感', datetime('now', 'localtime'), datetime('now', 'localtime')),
('恥ずかしさ', datetime('now', 'localtime'), datetime('now', 'localtime')),
('怒り', datetime('now', 'localtime'), datetime('now', 'localtime')),
('悲しみ', datetime('now', 'localtime'), datetime('now', 'localtime')),
('落ち込み', datetime('now', 'localtime'), datetime('now', 'localtime')),
('恐れ', datetime('now', 'localtime'), datetime('now', 'localtime')),
('イライラ', datetime('now', 'localtime'), datetime('now', 'localtime')),
('疲れ', datetime('now', 'localtime'), datetime('now', 'localtime')),
('罪悪感', datetime('now', 'localtime'), datetime('now', 'localtime'));

-- Initial data for positive_feels table
INSERT OR IGNORE INTO positive_feels (positive_feel_name, created_at, updated_at)
VALUES
('愛情', datetime('now', 'localtime'), datetime('now', 'localtime')),
('熱意', datetime('now', 'localtime'), datetime('now', 'localtime')),
('自由', datetime('now', 'localtime'), datetime('now', 'localtime')),
('喜び', datetime('now', 'localtime'), datetime('now', 'localtime')),
('落ち着いている', datetime('now', 'localtime'), datetime('now', 'localtime')),
('充実感', datetime('now', 'localtime'), datetime('now', 'localtime')),
('好奇心', datetime('now', 'localtime'), datetime('now', 'localtime')),
('ワクワク', datetime('now', 'localtime'), datetime('now', 'localtime')),
('自信がある', datetime('now', 'localtime'), datetime('now', 'localtime')),
('安心', datetime('now', 'localtime'), datetime('now', 'localtime')),
('やる気がある', datetime('now', 'localtime'), datetime('now', 'localtime')),
('感謝', datetime('now', 'localtime'), datetime('now', 'localtime')),
('思いやり', datetime('now', 'localtime'), datetime('now', 'localtime')),
('感動', datetime('now', 'localtime'), datetime('now', 'localtime'));

-- Initial data for distortion_lists table
INSERT OR IGNORE INTO distortion_lists (distortion_name, info, created_at, updated_at)
VALUES
('白黒思考', '「完璧にやろうとしていない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('過度な一般化', '「一度の失敗で、また同じ事が起こると思っていない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('心のフィルター', '「悪い点だけ目についていない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('マイナス化思考', '「自分を素直に褒めることができない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('読心術', '「相手のことを思い込んでいない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('運命の先読み', '「私は一生不幸だ、と思いすぎていない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('破滅化', '「すこしの失敗を大げさにとらえすぎていない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('感情的決めつけ', '「感情に巻き込まれていない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('べき思考', '「自分や相手に厳しすぎない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('レッテル貼り', '「私はこういう人間だ、と思いすぎていない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('内なる批評家', '「自分を責めすぎていない？」', datetime('now', 'localtime'), datetime('now', 'localtime')),
('他人批判', '「あいつのせい、って思いすぎていない？」', datetime('now', 'localtime'), datetime('now', 'localtime'));
