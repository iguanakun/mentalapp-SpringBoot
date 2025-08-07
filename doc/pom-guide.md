# MentalApp Maven依存関係ガイド

## 目次

1. [概要](#概要)
2. [プロジェクト設定](#プロジェクト設定)
3. [主要な依存関係](#主要な依存関係)
4. [ビルド設定](#ビルド設定)
5. [既知の問題と解決策](#既知の問題と解決策)

## 概要

このドキュメントでは、MentalAppアプリケーションのMaven依存関係管理について説明します。`pom.xml`ファイルは、プロジェクトの依存関係、ビルド設定、プロパティなどを定義するMavenの中心的な設定ファイルです。

## プロジェクト設定

```xml
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
<n>demosecurity</n>
<description>Demo project for Spring Boot</description>
```

### 設定の説明

- **親プロジェクト**: Spring Boot Starter Parent (バージョン3.4.3)を親プロジェクトとして使用しています。これにより、Spring Bootの推奨依存関係バージョンとプラグイン設定が継承されます。
- **グループID**: `com.luv2code.springboot` - プロジェクトの論理的なグループ名
- **アーティファクトID**: `demo` - プロジェクトの成果物名
- **バージョン**: `0.0.1-SNAPSHOT` - 開発中のバージョン
- **名前**: `demosecurity` - プロジェクトの表示名
- **説明**: `Demo project for Spring Boot` - プロジェクトの説明

### 注意点

現在の`pom.xml`ファイルには、XMLの構文エラーがあります。14行目に`<n>demosecurity</n>`と記述されていますが、正しくは`<name>demosecurity</name>`です。この問題は修正が必要です。

## プロパティ

```xml
<properties>
    <java.version>22</java.version>
    <mybatis.version>3.5.19</mybatis.version>
</properties>
```

### 設定の説明

- **java.version**: Java 22を使用しています。これは最新のJavaバージョンであり、最新の言語機能を利用できます。
- **mybatis.version**: MyBatis 3.5.19を使用しています。これはMyBatisの最新バージョンであり、安定性と新機能を提供します。

## 主要な依存関係

### Spring Boot関連

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

#### 依存関係の説明

1. **spring-boot-starter-data-jpa**
   - **目的**: JPAを使用したデータアクセス層の実装を提供
   - **主な機能**: エンティティ管理、リポジトリパターン、トランザクション管理
   - **必要な理由**: データベースとのやり取りを簡素化し、オブジェクト関係マッピング（ORM）を提供するため

2. **spring-boot-starter-security**
   - **目的**: アプリケーションのセキュリティ機能を提供
   - **主な機能**: 認証、認可、CSRF対策、セッション管理
   - **必要な理由**: ユーザー認証と認可を実装し、アプリケーションを保護するため

3. **spring-boot-starter-thymeleaf**
   - **目的**: Thymeleafテンプレートエンジンを提供
   - **主な機能**: サーバーサイドのHTMLテンプレート処理
   - **必要な理由**: 動的なWebページを生成するため

4. **spring-boot-starter-web**
   - **目的**: Webアプリケーション開発のための基本機能を提供
   - **主な機能**: Spring MVC、RESTful API、組み込みTomcat
   - **必要な理由**: Webアプリケーションの基盤として必要

5. **spring-boot-starter-validation**
   - **目的**: データバリデーション機能を提供
   - **主な機能**: Bean Validation API、Hibernateバリデーター
   - **必要な理由**: フォーム入力やデータの検証を行うため

### データベース関連

```xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>3.0.5</version>
</dependency>
```

#### 依存関係の説明

1. **mysql-connector-j**
   - **目的**: MySQLデータベースへの接続を提供
   - **主な機能**: JDBCドライバー
   - **必要な理由**: MySQLデータベースと通信するため
   - **スコープ**: runtime（実行時のみ必要）

2. **mybatis-spring-boot-starter**
   - **目的**: MyBatisとSpring Bootの統合を提供
   - **主な機能**: SQLマッパー、データアクセスオブジェクト
   - **必要な理由**: SQLを直接記述してデータベース操作を行うため
   - **バージョン**: 3.0.5（Spring Boot 3.xと互換性のあるバージョン）

### その他の依存関係

```xml
<dependency>
    <groupId>org.thymeleaf.extras</groupId>
    <artifactId>thymeleaf-extras-springsecurity6</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
```

#### 依存関係の説明

1. **thymeleaf-extras-springsecurity6**
   - **目的**: ThymeleafテンプレートでSpring Securityの機能を使用できるようにする
   - **主な機能**: セキュリティ関連のThymeleafタグライブラリ
   - **必要な理由**: ビューでユーザーの認証状態や権限に基づいた表示制御を行うため

2. **spring-boot-devtools**
   - **目的**: 開発者の生産性を向上させる
   - **主な機能**: 自動再起動、ライブリロード
   - **必要な理由**: 開発中のコード変更を即座に反映するため
   - **スコープ**: runtime（実行時のみ必要）
   - **オプション**: true（依存関係の推移的な継承を防ぐ）

3. **spring-boot-starter-test**
   - **目的**: テスト機能を提供
   - **主な機能**: JUnit、Mockito、Spring Testなど
   - **必要な理由**: アプリケーションのユニットテストと統合テストを行うため
   - **スコープ**: test（テスト時のみ必要）

4. **spring-security-test**
   - **目的**: Spring Securityのテスト機能を提供
   - **主な機能**: セキュリティコンテキストのモック、テストユーザーの設定
   - **必要な理由**: セキュリティ機能を含むテストを行うため
   - **スコープ**: test（テスト時のみ必要）

## ビルド設定

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```

### 設定の説明

- **spring-boot-maven-plugin**: Spring Bootアプリケーションをビルドするためのプラグイン
  - 実行可能なJARファイルの作成
  - 依存関係の管理
  - Spring Bootアプリケーションの実行

## 既知の問題と解決策

### 1. XMLの構文エラー

**問題**: `pom.xml`ファイルの14行目に`<n>demosecurity</n>`というタグがあります。これは正しいXML構文ではありません。

**解決策**: 以下のように修正する必要があります：

```xml
<!-- 誤り -->
<n>demosecurity</n>

<!-- 正しい -->
<name>demosecurity</name>
```

### 2. Jakarta Persistence APIの依存関係

**問題**: 現在の`pom.xml`では、`spring-boot-starter-data-jpa`がコメントアウトされ、代わりに`jakarta.persistence-api`が直接追加されています。これは不完全な設定です。

**解決策**: `spring-boot-starter-data-jpa`を有効にすることで、必要なすべての依存関係（Jakarta Persistence API、Hibernate、HikariCP など）が自動的に含まれます：

```xml
<!-- 推奨設定 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

### 3. Maven依存関係の更新

IntelliJ IDEAでMaven依存関係を更新するには：

1. プロジェクトエクスプローラーで`pom.xml`を右クリック
2. 「Maven」→「プロジェクトの再読み込み」を選択

または：

1. Mavenツールウィンドウを開く（表示→ツールウィンドウ→Maven）
2. 「すべてのMavenプロジェクトを再読み込み」ボタン（円形の矢印アイコン）をクリック

これにより、修正された`pom.xml`ファイルに基づいて依存関係が再解決されます。