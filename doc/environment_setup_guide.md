# MentalApp 環境構築・設定ガイド

## 目次

1. [プロジェクト概要](#プロジェクト概要)
2. [技術スタック](#技術スタック)
3. [開発環境のセットアップ](#開発環境のセットアップ)
4. [Docker環境](#docker環境)
5. [データベース構成](#データベース構成)
6. [アプリケーション設定](#アプリケーション設定)
7. [デプロイ手順](#デプロイ手順)
8. [トラブルシューティング](#トラブルシューティング)

## プロジェクト概要

MentalAppはSpring Bootを使用した認証機能を持つWebアプリケーションです。ユーザー管理、ロールベースのアクセス制御、セキュリティ機能を実装しています。このアプリケーションはDocker環境で実行され、MySQLデータベースと連携します。

## 技術スタック

### バックエンド
- **Java**: バージョン22
- **Spring Boot**: バージョン3.4.3
- **Spring Security**: 認証・認可フレームワーク
- **MyBatis**: バージョン3.5.19、SQLマッパーフレームワーク
- **Jakarta Persistence API**: エンティティ管理用API

### データベース
- **MySQL**: バージョン8.4.5
- **HikariCP**: コネクションプール

### フロントエンド
- **Thymeleaf**: サーバーサイドJavaテンプレートエンジン
- **Thymeleaf Extras Spring Security**: セキュリティ統合

### ビルド・デプロイ
- **Maven**: 依存関係管理とビルドツール
- **Docker**: コンテナ化
- **Docker Compose**: マルチコンテナ管理

## 開発環境のセットアップ

### 前提条件
- JDK 22
- Maven 3.9以上
- Docker および Docker Compose
- Git

### ローカル開発環境のセットアップ

1. **リポジトリのクローン**:
   ```bash
   git clone <repository-url>
   cd mentalapp-SpringBoot
   ```

2. **Mavenの依存関係をインストール**:
   ```bash
   ./mvnw clean install
   ```

3. **アプリケーションの実行（ローカル）**:
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Docker環境での実行**:
   ```bash
   docker-compose -f docker-compose.optimized.yml up -d
   ```

## Docker環境

### 構成ファイル

#### docker-compose.optimized.yml
最適化されたDocker Compose設定ファイルで、以下の特徴があります：

- **環境変数の活用**: すべての設定値を環境変数から取得
- **ヘルスチェック**: コンテナの健全性監視
- **明示的な命名**: ボリュームとネットワークに名前を付与
- **リスタートポリシー**: 適切な再起動設定

```yaml
version: '3.8'
services:
  web:
    container_name: ${APP_CONTAINER_NAME}
    build:
      dockerfile: Dockerfile.optimized
      context: .
    ports:
      - "${SERVER_PORT}:${SERVER_PORT}"
    environment:
      # Spring Boot データベース接続設定
      SPRING_DATASOURCE_URL: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true
      SPRING_DATASOURCE_USERNAME: ${DB_ROOT_PASSWORD}
      SPRING_DATASOURCE_PASSWORD: ${DB_ROOT_PASSWORD}
      # アプリケーション設定
      SERVER_PORT: ${SERVER_PORT}
      JAVA_OPTS: ${JAVA_OPTS}
      SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}
    depends_on:
      db:
        condition: service_started
    networks:
      - app-net
    restart: unless-stopped
    healthcheck:
      test: ["CMD", "wget", "-q", "--spider", "http://localhost:${SERVER_PORT}/actuator/health"]
      interval: 30s
      timeout: 5s
      retries: 3
      start_period: 40s
```

#### Dockerfile.optimized
マルチステージビルドを実装し、ビルド環境と実行環境を分離しています：

```dockerfile
# ビルドステージ
FROM maven:3.9-eclipse-temurin-22 AS build

# 作業ディレクトリの設定
WORKDIR /build

# 依存関係のキャッシュを最適化するため、pom.xmlを先にコピー
COPY pom.xml .

# 依存関係のダウンロードのみを実行（ソースなしでも可能）
RUN mvn dependency:go-offline

# ソースコードのコピー
COPY src src

# Mavenビルドの実行
RUN mvn package -DskipTests

# 実行ステージ
FROM eclipse-temurin:22-jre

# 環境変数の設定（デフォルト値）
ENV APP_HOME=/app \
    SERVER_PORT=8080 \
    JAVA_OPTS=""

# 作業ディレクトリの設定
WORKDIR ${APP_HOME}

# ビルドステージからJARファイルのみをコピー
COPY --from=build /build/target/*.jar app.jar

# ポートの公開（環境変数を使用）
EXPOSE ${SERVER_PORT}

# ヘルスチェック
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -q --spider http://localhost:${SERVER_PORT}/actuator/health || exit 1

# アプリケーションの実行（環境変数を使用）
CMD java ${JAVA_OPTS} -Dserver.port=${SERVER_PORT} -jar app.jar
```

#### .env.optimized
環境変数を一元管理するファイルです：

```properties
# データベース設定
DB_HOST=db
DB_PORT=3306
DB_NAME=mentalapp
DB_ROOT_PASSWORD=root
DB_USER=user
DB_PASSWORD=password

# MySQLコンテナ設定
MYSQL_CONTAINER_NAME=mentaldb
MYSQL_EXTERNAL_PORT=13306
MYSQL_VERSION=8.4.5
TZ=Asia/Tokyo

# アプリケーション設定
APP_CONTAINER_NAME=mentalapp
SERVER_PORT=8080
JAVA_OPTS=-Xmx512m -Xms256m

# Spring設定
SPRING_PROFILES_ACTIVE=prod
```

### 設定の意味

#### Docker Compose設定

| 設定項目 | 説明 |
|---------|------|
| `version: '3.8'` | Docker Composeファイルのバージョン指定 |
| `services` | コンテナサービスの定義セクション |
| `container_name` | コンテナの名前を指定 |
| `build.dockerfile` | ビルドに使用するDockerfileを指定 |
| `ports` | ホストとコンテナのポートマッピング |
| `environment` | コンテナ内の環境変数設定 |
| `depends_on` | サービス間の依存関係を定義 |
| `networks` | コンテナが接続するネットワークを指定 |
| `restart` | コンテナの再起動ポリシー |
| `healthcheck` | コンテナの健全性チェック設定 |
| `volumes` | データの永続化設定 |

#### Dockerfile設定

| 設定項目 | 説明 |
|---------|------|
| `FROM` | ベースイメージの指定 |
| `WORKDIR` | 作業ディレクトリの設定 |
| `COPY` | ファイルのコピー |
| `RUN` | コマンドの実行 |
| `ENV` | 環境変数の設定 |
| `EXPOSE` | 公開ポートの指定 |
| `CMD` | コンテナ起動時のコマンド |
| `HEALTHCHECK` | ヘルスチェック設定 |

## データベース構成

### データベース設計

MentalAppでは、以下のテーブルを使用しています：

#### user テーブル
ユーザー情報を格納します。

| カラム名 | データ型 | 説明 |
|---------|---------|------|
| id | int | 主キー、自動採番 |
| username | varchar(50) | ユーザー名（ログインID） |
| password | char(80) | BCryptでハッシュ化されたパスワード |
| enabled | tinyint | アカウントの有効/無効状態 |
| first_name | varchar(64) | 名 |
| last_name | varchar(64) | 姓 |
| email | varchar(64) | メールアドレス |

#### role テーブル
ロール（権限）情報を格納します。

| カラム名 | データ型 | 説明 |
|---------|---------|------|
| id | int | 主キー、自動採番 |
| name | varchar(50) | ロール名（例：ROLE_ADMIN） |

#### users_roles テーブル
ユーザーとロールの関連付けを格納する中間テーブルです。

| カラム名 | データ型 | 説明 |
|---------|---------|------|
| user_id | int | userテーブルの外部キー |
| role_id | int | roleテーブルの外部キー |

### 初期化スクリプト

`sql-scripts/03-create-mentalapp-tables.sql` ファイルは、Docker起動時に自動的に実行され、必要なテーブルとサンプルデータを作成します。

```sql
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

-- Table structure for table `user`
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

-- Table structure for table `role`
CREATE TABLE `role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- Table structure for table `users_roles` (中間テーブル)
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

-- サンプルデータの挿入
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
```

### MySQL設定

`mysql/mysql.cnf` ファイルでMySQLの設定を行っています。以下が設定内容です：

```ini
[mysqld]
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci
default-authentication-plugin = mysql_native_password

[client]
default-character-set=utf8mb4
```

#### 設定の意味

| 設定項目 | 説明 |
|---------|------|
| `character-set-server=utf8mb4` | データベースのデフォルト文字コードをUTF-8（4バイト）に設定 |
| `collation-server=utf8mb4_unicode_ci` | 照合順序（文字の比較方法）をUnicode互換に設定 |
| `default-authentication-plugin` | 認証プラグインを`mysql_native_password`に設定（旧バージョンとの互換性のため） |
| `default-character-set` | クライアント接続時の文字コードをUTF-8（4バイト）に設定 |

## アプリケーション設定

### application.properties

`src/main/resources/application.properties` ファイルには、アプリケーションの主要な設定が含まれています：

```properties
#
# JDBC Properties
#
#spring.datasource.url=jdbc:mysql://localhost:3306/employee_directory?useSSL=false&allowPublicKeyRetrieval=true
#spring.datasource.url=jdbc:mysql://localhost:13306/mentalapp?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.url=jdbc:mysql://db:3306/mentalapp?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

#
# MyBatis Properties
#
mybatis.mapper-locations=classpath*:/mapper/*.xml
mybatis.type-aliases-package=com.mentalapp.common.entity
mybatis.configuration.map-underscore-to-camel-case=true

#
# Log JDBC SQL statements
#
# Only use this for dev/testing
# DO NOT use for PRODUCTION since it will log user names
logging.level.org.springframework.jdbc.core=TRACE
```

### 設定の意味

| 設定項目 | 説明 |
|---------|------|
| `spring.datasource.url` | データベース接続URL |
| `spring.datasource.username` | データベース接続ユーザー名 |
| `spring.datasource.password` | データベース接続パスワード |
| `spring.datasource.driver-class-name` | JDBCドライバークラス |
| `mybatis.mapper-locations` | MyBatisマッパーXMLファイルの場所 |
| `mybatis.type-aliases-package` | エンティティクラスのパッケージ |
| `mybatis.configuration.map-underscore-to-camel-case` | カラム名のスネークケースをJavaのキャメルケースに自動変換 |
| `logging.level.org.springframework.jdbc.core` | SQLログレベル |

### Maven設定（pom.xml）

`pom.xml` ファイルには、プロジェクトの依存関係と構成が含まれています：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>3.4.3</version>
		<relativePath/> <!-- lookup parent from repository -->
	</parent>
	<groupId>com.luv2code.springboot</groupId>
	<artifactId>demo</artifactId>
	<version>0.0.1-SNAPSHOT</version>
	<name>demosecurity</name>
	<description>Demo project for Spring Boot</description>
	<properties>
		<java.version>22</java.version>
		<mybatis.version>3.5.19</mybatis.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>3.0.5</version>
		</dependency>

		<dependency>
			<groupId>com.mysql</groupId>
			<artifactId>mysql-connector-j</artifactId>
			<scope>runtime</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>
		<!-- その他の依存関係 -->
	</dependencies>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```

## デプロイ手順

### 開発環境

1. **環境変数ファイルの準備**:
   ```bash
   cp .env.optimized .env
   # 必要に応じて.envを編集
   ```

2. **Docker環境のビルドと起動**:
   ```bash
   docker-compose -f docker-compose.optimized.yml up -d --build
   ```

3. **アプリケーションの動作確認**:
   ```bash
   # ログの確認
   docker-compose -f docker-compose.optimized.yml logs -f web
   
   # アプリケーションにアクセス
   curl http://localhost:8080
   ```

### 本番環境

1. **環境変数の設定**:
   - 本番環境用の`.env`ファイルを作成
   - セキュリティを考慮したパスワードを設定

2. **Docker Composeの実行**:
   ```bash
   docker-compose -f docker-compose.optimized.yml up -d
   ```

3. **ヘルスチェックの確認**:
   ```bash
   docker inspect --format='{{.State.Health.Status}}' mentalapp
   docker inspect --format='{{.State.Health.Status}}' mentaldb
   ```

## トラブルシューティング

### よくある問題と解決策

#### 1. データベース接続エラー

**症状**: アプリケーションログに「Could not create connection to database server」などのエラーが表示される

**解決策**:
- データベースコンテナが起動しているか確認
  ```bash
  docker-compose ps
  ```
- データベース接続設定が正しいか確認
  ```bash
  # application.propertiesの確認
  cat src/main/resources/application.properties
  
  # 環境変数の確認
  docker exec mentalapp env | grep SPRING_DATASOURCE
  ```
- ネットワーク接続を確認
  ```bash
  docker exec mentalapp ping -c 3 db
  ```

#### 2. アプリケーションが起動しない

**症状**: Webコンテナが起動しない、またはすぐに終了する

**解決策**:
- ログを確認
  ```bash
  docker-compose logs web
  ```
- JARファイルが正しく生成されているか確認
  ```bash
  docker exec -it mentalapp ls -la /app
  ```
- Javaバージョンを確認
  ```bash
  docker exec -it mentalapp java -version
  ```

#### 3. Maven依存関係の問題

**症状**: ビルド時に「Could not resolve dependencies」などのエラーが表示される

**解決策**:
- pom.xmlの構文を確認
- Mavenリポジトリをクリーンアップ
  ```bash
  ./mvnw dependency:purge-local-repository
  ```
- Maven設定を更新
  ```bash
  ./mvnw clean install -U
  ```

### ログの確認方法

```bash
# アプリケーションログの確認
docker-compose logs web

# データベースログの確認
docker-compose logs db

# リアルタイムでログを追跡
docker-compose logs -f

# 特定のコンテナのログを確認
docker logs mentalapp
docker logs mentaldb
```

### データベースの直接操作

```bash
# MySQLコンテナに接続
docker exec -it mentaldb mysql -uroot -proot

# 特定のクエリを実行
docker exec -it mentaldb mysql -uroot -proot -e "USE mentalapp; SHOW TABLES;"

# データベースのダンプ
docker exec -it mentaldb mysqldump -uroot -proot mentalapp > backup.sql
```

---

このドキュメントは、MentalAppプロジェクトの環境構築と設定に関する包括的なガイドです。プロジェクトの変更に応じて、このドキュメントも更新してください。