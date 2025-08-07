# MentalApp - Spring Boot アプリケーション

## データベース初期化について

このアプリケーションでは、Docker起動時に自動的にデータベーステーブルが作成されるように設定されています。

### 実装内容

1. **SQLスクリプトの作成**
   - `sql-scripts/03-create-mentalapp-tables.sql` ファイルを作成
   - entityパッケージのクラス（User.javaとRole.java）に基づいたテーブル定義
   - サンプルデータの挿入（管理者ユーザーと一般ユーザー）

2. **Docker設定の変更**
   - `docker-compose.yml` ファイルを修正
   - SQLスクリプトをMySQLコンテナの初期化ディレクトリ（`/docker-entrypoint-initdb.d/`）にマウント
   - コンテナ起動時に自動的にSQLスクリプトが実行される設定

### 作成されるテーブル

1. **user テーブル**
   - ユーザー情報を格納
   - フィールド: id, username, password, enabled, first_name, last_name, email

2. **role テーブル**
   - ロール（権限）情報を格納
   - フィールド: id, name

3. **users_roles テーブル**
   - ユーザーとロールの関連付けを格納する中間テーブル
   - フィールド: user_id, role_id

### サンプルデータ

初期データとして以下のユーザーが作成されます：

1. **管理者ユーザー**
   - ユーザー名: admin
   - パスワード: password
   - ロール: ROLE_EMPLOYEE, ROLE_MANAGER, ROLE_ADMIN

2. **一般ユーザー**
   - ユーザー名: user
   - パスワード: password
   - ロール: ROLE_EMPLOYEE

## アプリケーションの起動方法

```bash
# Dockerコンテナを起動
docker-compose up -d
```

初回起動時にデータベースが自動的に初期化されます。