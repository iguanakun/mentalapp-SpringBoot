# MentalApp - 認知行動療法セルフケアアプリ

## 概要

本アプリは、認知行動療法(Cognitive Behavioral Therapy)の技法を用いたメンタルヘルスのためのセルフケアアプリ。  
ユーザはストレス体験について上手に付き合うための心理療法を、アプリを通じて実践することができる。

## アクセス情報

### URL

後日記載

### テスト用アカウント

- ユーザー名: `mentalapp`
- パスワード: `mentalapp`

## 開発背景

近年「ネガティブ・ケイパビリティ」と呼ばれる「モヤモヤする力」が注目されている。  
ビジネスシーンでは、問題に対して素早く効率的に結論を出すことが求められる。  
この延長線上で、日常生活の様々な問題についてもすぐに答えを出すことが求められる、そんな傾向があるように感じる。  
しかし人間関係、恋愛、自分がやりたい仕事などの悩みは答えが出ない問題である。  
人生の転機――受験、就職、出産、育児といった出来事ではどんな人でも不安を感じ、時には心の不調となってしまう場合もあるだろう。  
このような答えが出ない問題は「悩むのは当たり前」で、答えを出すことを保留にしてもいい。  
答えを出すために一歩立ち止まって、悩み続けて、問題に対処する力を身に着ける方法を広めたい。  
自分でできる心理療法を世の中に提供したい、という思いからアプリを開発した。

## 主な機能と利用方法

トップページのヘッダーからユーザー新規登録を行う。  
登録後に「エクササイズをする」ボタンを押すと、それぞれの心理療法を実施できる。

## 技術スタック

### フロントエンド

- **HTML/CSS**, JavaScript

### バックエンド

- **Java 21** (Amazon Corretto)
- **Spring Boot 3.4.3**
- **MyBatis 3.0.5**
- **JUnit 5**

### データベース

- **MySQL 8.4.5** (ローカル開発環境)
- **SQLite** (本番環境 - EFS上)

### インフラ

AWS環境に構築:

- **EC2** - Spot Instanceによる費用最適化
- **EFS** - SQLiteデータベース用
- **EventBridge Scheduler** - EC2の自動起動・停止

### 開発ツール

- **Terraform**
- **Docker**

## アーキテクチャ

### AWS構成図

![AWS Architecture](doc/aws-architecture.drawio)

### データベース設計

![ER Diagram](doc/er-diagram.drawio)

## セットアップ手順

### ローカル開発環境

#### 1. リポジトリのクローン

```bash
git clone https://github.com/iguanakun/mental-app.git
cd mentalapp-SpringBoot
```

#### 2. 環境変数設定

```bash
cp .env.sample .env
# .env ファイルを編集してデータベース設定を変更(必要に応じて)
```

#### 3. Docker Composeでアプリケーション起動

```bash
docker-compose up -d
```

- MySQL: `localhost:13306`
- アプリケーション: `http://localhost:8080`

#### 4. ビルド

```bash
mvn clean package
```

### 本番環境デプロイ

#### Terraformによるインフラ構築

```bash
cd terraform

# インフラ構築
terraform init
terraform plan
terraform apply

# インフラ削除
terraform destroy
```

## ディレクトリ構造

```
mentalapp-SpringBoot/
├── src/
│   ├── main/
│   │   ├── java/com/mentalapp/
│   │   │   ├── cbt_basic/         # CBT基本モニタリング
│   │   │   ├── cbt_cr/            # 認知再構成
│   │   │   ├── common/            # 共通機能
│   │   │   ├── user_memo_list/    # 記録一覧
│   │   │   └── top/               # トップページ
│   │   └── resources/
│   │       ├── mapper/            # MyBatis XMLマッパー
│   │       ├── templates/         # Thymeleafテンプレート
│   │       ├── static/            # CSS, JS, 画像
│   │       ├── init-sqlite.sql    # SQLite初期化スクリプト
│   │       ├── application.properties
│   │       └── application-ecs.properties
│   └── test/                      # テストコード
├── terraform/                     # Terraformインフラコード
│   ├── modules/
│   │   ├── ec2_spot/              # EC2 Spot Instance
│   │   ├── ec2_scheduler/         # EventBridge Scheduler
│   │   ├── efs/                   # Amazon EFS
│   │   ├── security_group/        # Security Groups
│   │   └── vpc/                   # VPC
│   └── main.tf
├── deploy/                        # デプロイスクリプト
│   └── scripts.sh                 # EC2初期化スクリプト
├── doc/                           # ドキュメント
│   ├── er-diagram.drawio          # ER図
│   └── aws-architecture.drawio    # AWS構成図
├── docker-compose.yml             # ローカル開発環境
├── Dockerfile                     # Dockerイメージ定義
├── .env.sample                    # 環境変数テンプレート
└── pom.xml                        # Maven設定
```
