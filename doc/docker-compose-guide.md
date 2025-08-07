# Docker Compose 設定ガイド

このドキュメントでは、MentalAppプロジェクトで使用されているDocker Compose設定について詳細に説明します。Docker Composeは複数のコンテナを定義し、実行するためのツールであり、アプリケーションの開発環境と本番環境の両方で使用されています。

## 目次

1. [Docker Composeの概要](#docker-composeの概要)
2. [基本構成ファイル (docker-compose.yml)](#基本構成ファイル-docker-composeyml)
3. [最適化された構成ファイル (docker-compose.optimized.yml)](#最適化された構成ファイル-docker-composeoptimizedyml)
4. [環境変数の管理](#環境変数の管理)
5. [ネットワーク設定](#ネットワーク設定)
6. [ボリューム設定](#ボリューム設定)
7. [ヘルスチェック](#ヘルスチェック)
8. [トラブルシューティング](#トラブルシューティング)

## Docker Composeの概要

Docker Composeは、複数のDockerコンテナを定義し、実行するためのツールです。YAMLファイルを使用して、サービス、ネットワーク、ボリュームなどを設定します。MentalAppプロジェクトでは、以下の目的でDocker Composeを使用しています：

- Webアプリケーション（Spring Boot）とデータベース（MySQL）の連携
- 開発環境と本番環境の一貫性の確保
- コンテナ間の通信の管理
- データの永続化
- アプリケーションの自動起動と再起動

## 基本構成ファイル (docker-compose.yml)

`docker-compose.yml`は基本的な構成ファイルで、以下の主要コンポーネントで構成されています：

```yaml
version: '3.8'
services:
  web:
    container_name: mentalapp
    build:
      dockerfile: Dockerfile
      context: .
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: ${DB_URL}
      SPRING_DATASOURCE_USERNAME: ${DB_USER}
      SPRING_DATASOURCE_PASSWORD: ${DB_PASS}
    depends_on:
      db:
        condition: service_started
    networks:
      - app-net
  db:
    image: mysql:8.4.5
    container_name: mentaldb
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: ${ROOT_PASS}
      MYSQL_DATABASE: ${DB_NAME}
      MYSQL_USER: ${DB_USER}
      MYSQL_PASSWORD: ${DB_PASS}
      TZ: ${TZ}
    ports:
      - ${DB_PORT}:3306
    volumes:
      - db-store:/var/lib/mysql
      - ./mysql/mysql.cnf:/etc/mysql/conf.d/my.cnf
      - ./sql-scripts/03-create-mentalapp-tables.sql:/docker-entrypoint-initdb.d/03-create-mentalapp-tables.sql
    networks:
      - app-net
volumes:
  db-store:

networks:
  app-net:
    driver: bridge
```

### 各設定の詳細と必要性

#### バージョン設定
```yaml
version: '3.8'
```
- **説明**: Docker Composeファイルのバージョンを指定します。
- **必要性**: 新しい機能や構文を使用するために必要です。バージョン3.xはSwarmモードとの互換性があります。

#### Webサービス設定

```yaml
web:
  container_name: mentalapp
  build:
    dockerfile: Dockerfile
    context: .
  ports:
    - "8080:8080"
  environment:
    SPRING_DATASOURCE_URL: ${DB_URL}
    SPRING_DATASOURCE_USERNAME: ${DB_USER}
    SPRING_DATASOURCE_PASSWORD: ${DB_PASS}
  depends_on:
    db:
      condition: service_started
  networks:
    - app-net
```

- **container_name**: コンテナに明示的な名前を付けます。これにより、ログの確認やコンテナの操作が容易になります。
- **build**: Dockerfileの場所とビルドコンテキストを指定します。これにより、アプリケーションのビルド方法を定義できます。
- **ports**: ホストとコンテナのポートマッピングを設定します。「8080:8080」はホストの8080ポートをコンテナの8080ポートにマッピングします。
- **environment**: コンテナ内の環境変数を設定します。Spring Bootのデータベース接続設定を環境変数で渡しています。
- **depends_on**: サービスの依存関係を定義します。webサービスはdbサービスが起動した後に開始されます。
- **networks**: コンテナが接続するネットワークを指定します。app-netネットワークを使用して、webとdbコンテナ間の通信を可能にします。

#### データベースサービス設定

```yaml
db:
  image: mysql:8.4.5
  container_name: mentaldb
  restart: always
  environment:
    MYSQL_ROOT_PASSWORD: ${ROOT_PASS}
    MYSQL_DATABASE: ${DB_NAME}
    MYSQL_USER: ${DB_USER}
    MYSQL_PASSWORD: ${DB_PASS}
    TZ: ${TZ}
  ports:
    - ${DB_PORT}:3306
  volumes:
    - db-store:/var/lib/mysql
    - ./mysql/mysql.cnf:/etc/mysql/conf.d/my.cnf
    - ./sql-scripts/03-create-mentalapp-tables.sql:/docker-entrypoint-initdb.d/03-create-mentalapp-tables.sql
  networks:
    - app-net
```

- **image**: 使用するDockerイメージを指定します。MySQL 8.4.5を使用しています。
- **container_name**: コンテナに明示的な名前を付けます。
- **restart**: コンテナの再起動ポリシーを設定します。「always」はコンテナが停止した場合に常に再起動します。
- **environment**: MySQLの設定に必要な環境変数を設定します。
  - `MYSQL_ROOT_PASSWORD`: rootユーザーのパスワード
  - `MYSQL_DATABASE`: 作成するデータベース名
  - `MYSQL_USER`: 作成する追加ユーザー名
  - `MYSQL_PASSWORD`: 追加ユーザーのパスワード
  - `TZ`: タイムゾーン設定
- **ports**: ホストとコンテナのポートマッピングを設定します。環境変数`DB_PORT`で指定されたポートをコンテナの3306ポートにマッピングします。
- **volumes**: データの永続化とファイルのマウントを設定します。
  - `db-store:/var/lib/mysql`: データベースファイルを永続化するためのボリューム
  - `./mysql/mysql.cnf:/etc/mysql/conf.d/my.cnf`: MySQLの設定ファイルをマウント
  - `./sql-scripts/03-create-mentalapp-tables.sql:/docker-entrypoint-initdb.d/03-create-mentalapp-tables.sql`: 初期化スクリプトをマウント（コンテナ起動時に自動実行）
- **networks**: コンテナが接続するネットワークを指定します。

#### ボリューム設定

```yaml
volumes:
  db-store:
```

- **説明**: 名前付きボリュームを定義します。
- **必要性**: データベースのデータを永続化するために必要です。コンテナが削除されても、データは保持されます。

#### ネットワーク設定

```yaml
networks:
  app-net:
    driver: bridge
```

- **説明**: コンテナ間の通信に使用するネットワークを定義します。
- **必要性**: webコンテナとdbコンテナが互いに通信できるようにするために必要です。
- **driver**: ネットワークドライバーを指定します。「bridge」はデフォルトのドライバーで、同じホスト上のコンテナ間の通信に使用されます。

## 最適化された構成ファイル (docker-compose.optimized.yml)

`docker-compose.optimized.yml`は、基本構成ファイルを拡張し、以下の最適化が施されています：

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

### 最適化された設定の詳細と必要性

#### 環境変数の拡張使用

```yaml
container_name: ${APP_CONTAINER_NAME}
ports:
  - "${SERVER_PORT}:${SERVER_PORT}"
```

- **説明**: コンテナ名やポート番号などの設定を環境変数から取得します。
- **必要性**: 環境ごとの設定変更を容易にし、設定の一元管理を可能にします。

#### 詳細なデータベース接続設定

```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useSSL=false&allowPublicKeyRetrieval=true
```

- **説明**: データベース接続URLを環境変数から構築し、追加のパラメータを含めます。
- **必要性**: SSL設定や公開鍵取得の設定など、接続の詳細を制御するために必要です。

#### アプリケーション設定の追加

```yaml
SERVER_PORT: ${SERVER_PORT}
JAVA_OPTS: ${JAVA_OPTS}
SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE}
```

- **説明**: アプリケーションのポート、Javaオプション、Spring Bootのプロファイルを設定します。
- **必要性**: アプリケーションの動作をカスタマイズし、環境ごとに適切な設定を適用するために必要です。

#### 再起動ポリシーの改善

```yaml
restart: unless-stopped
```

- **説明**: コンテナが異常終了した場合に再起動しますが、明示的に停止された場合は再起動しません。
- **必要性**: 自動復旧機能を提供しつつ、意図的な停止を尊重するために必要です。

#### ヘルスチェックの追加

```yaml
healthcheck:
  test: ["CMD", "wget", "-q", "--spider", "http://localhost:${SERVER_PORT}/actuator/health"]
  interval: 30s
  timeout: 5s
  retries: 3
  start_period: 40s
```

- **説明**: コンテナの健全性を定期的にチェックします。
- **必要性**: アプリケーションが正常に動作しているかを監視し、問題が発生した場合に検出するために必要です。
- **パラメータ**:
  - `test`: 実行するヘルスチェックコマンド
  - `interval`: チェックの間隔
  - `timeout`: チェックのタイムアウト時間
  - `retries`: 失敗と判断するまでの再試行回数
  - `start_period`: 初期化中の猶予期間

#### データベースのヘルスチェック

```yaml
healthcheck:
  test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-p${DB_ROOT_PASSWORD}"]
  interval: 10s
  timeout: 5s
  retries: 5
  start_period: 30s
```

- **説明**: MySQLデータベースの健全性をチェックします。
- **必要性**: データベースが応答しているかを確認し、問題が発生した場合に検出するために必要です。

#### 明示的なボリューム名とネットワーク名

```yaml
volumes:
  db-store:
    name: mentalapp-db-data

networks:
  app-net:
    driver: bridge
    name: mentalapp-network
```

- **説明**: ボリュームとネットワークに明示的な名前を付けます。
- **必要性**: 複数のプロジェクトが存在する環境での名前の衝突を避け、リソースの識別を容易にするために必要です。

## 環境変数の管理

Docker Composeファイルでは、環境変数を使用して設定を外部化しています。これにより、環境ごとに異なる設定を適用できます。

### .env ファイル

`.env`ファイルは、Docker Composeが自動的に読み込む環境変数ファイルです。以下は最適化された`.env.optimized`ファイルの例です：

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

### 環境変数の使用理由

1. **設定の一元管理**: すべての設定値を一箇所で管理できます。
2. **環境ごとの設定変更**: 開発、テスト、本番環境で異なる設定を簡単に適用できます。
3. **機密情報の分離**: パスワードなどの機密情報をバージョン管理から分離できます。
4. **コードの再利用性**: 同じDocker Compose設定を異なる環境で再利用できます。

## ネットワーク設定

Docker Composeでは、コンテナ間の通信を管理するためにネットワークを使用します。

```yaml
networks:
  app-net:
    driver: bridge
    name: mentalapp-network
```

### ネットワーク設定の必要性

1. **コンテナ間通信**: webコンテナとdbコンテナが互いに通信できるようにします。
2. **ネットワーク分離**: プロジェクト固有のネットワークを作成し、他のコンテナとの分離を確保します。
3. **名前解決**: コンテナ名をホスト名として使用できるようにします（例：`db`ホスト名でデータベースコンテナにアクセス）。

### ネットワークドライバー

- **bridge**: 同じホスト上のコンテナ間の通信に使用される標準のネットワークドライバーです。
- **host**: コンテナがホストのネットワークスタックを直接使用します。
- **overlay**: 複数のDocker hostsにまたがるSwarmサービス間の通信に使用されます。
- **macvlan**: コンテナに物理ネットワークインターフェースのようなMACアドレスを割り当てます。
- **none**: すべてのネットワーキングを無効にします。

MentalAppでは、標準的なユースケースに適した`bridge`ドライバーを使用しています。

## ボリューム設定

Docker Composeでは、データの永続化とファイルの共有にボリュームを使用します。

```yaml
volumes:
  db-store:
    name: mentalapp-db-data
```

### ボリューム設定の必要性

1. **データの永続化**: コンテナが削除されても、データは保持されます。
2. **パフォーマンス**: ボリュームはコンテナのファイルシステムよりも高速です。
3. **ホストとコンテナ間のファイル共有**: 設定ファイルやスクリプトをコンテナにマウントできます。

### ボリュームタイプ

- **名前付きボリューム**: `db-store`のような名前を持つボリュームで、Dockerによって管理されます。
- **バインドマウント**: ホストの特定のパスをコンテナにマウントします（例：`./mysql/mysql.cnf:/etc/mysql/conf.d/my.cnf`）。

### 初期化スクリプトのマウント

```yaml
./sql-scripts/03-create-mentalapp-tables.sql:/docker-entrypoint-initdb.d/03-create-mentalapp-tables.sql
```

- **説明**: ホスト上のSQLスクリプトをMySQLコンテナの初期化ディレクトリにマウントします。
- **必要性**: コンテナの初回起動時にデータベーススキーマとサンプルデータを自動的に作成するために必要です。
- **動作**: MySQLコンテナは起動時に`/docker-entrypoint-initdb.d/`ディレクトリ内のすべてのスクリプトを実行します。

## ヘルスチェック

Docker Composeの最適化された設定では、コンテナの健全性を監視するためのヘルスチェックが追加されています。

### Webアプリケーションのヘルスチェック

```yaml
healthcheck:
  test: ["CMD", "wget", "-q", "--spider", "http://localhost:${SERVER_PORT}/actuator/health"]
  interval: 30s
  timeout: 5s
  retries: 3
  start_period: 40s
```

- **説明**: Spring Boot Actuatorのヘルスエンドポイントにリクエストを送信して、アプリケーションの状態を確認します。
- **必要性**: アプリケーションが正常に動作しているかを監視し、問題が発生した場合に検出するために必要です。

### データベースのヘルスチェック

```yaml
healthcheck:
  test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u", "root", "-p${DB_ROOT_PASSWORD}"]
  interval: 10s
  timeout: 5s
  retries: 5
  start_period: 30s
```

- **説明**: `mysqladmin ping`コマンドを使用して、MySQLサーバーの応答を確認します。
- **必要性**: データベースが応答しているかを確認し、問題が発生した場合に検出するために必要です。

### ヘルスチェックパラメータ

- **test**: 実行するヘルスチェックコマンド
- **interval**: チェックの間隔（デフォルト: 30s）
- **timeout**: チェックのタイムアウト時間（デフォルト: 30s）
- **retries**: 失敗と判断するまでの再試行回数（デフォルト: 3）
- **start_period**: 初期化中の猶予期間（デフォルト: 0s）

## トラブルシューティング

Docker Compose環境で発生する可能性のある一般的な問題と解決策を以下に示します。

### コンテナが起動しない

**問題**: コンテナが起動しない、またはすぐに終了する。

**解決策**:
1. ログを確認する: `docker-compose logs [service_name]`
2. 環境変数が正しく設定されているか確認する
3. ポートの競合がないか確認する
4. ボリュームのパーミッションを確認する

### データベース接続エラー

**問題**: Webアプリケーションがデータベースに接続できない。

**解決策**:
1. データベースコンテナが実行中か確認する: `docker-compose ps`
2. データベース接続設定を確認する: `SPRING_DATASOURCE_URL`など
3. ネットワーク設定を確認する
4. データベースコンテナ内からの接続をテストする: `docker exec -it mentaldb mysql -uroot -proot`

### ボリュームの問題

**問題**: データが永続化されない、またはボリュームマウントが機能しない。

**解決策**:
1. ボリュームが正しく作成されているか確認する: `docker volume ls`
2. ボリュームのパスとパーミッションを確認する
3. 必要に応じてボリュームを再作成する: `docker-compose down -v && docker-compose up -d`

### ヘルスチェックの失敗

**問題**: コンテナのヘルスチェックが失敗する。

**解決策**:
1. ヘルスチェックのログを確認する: `docker inspect [container_name] | grep Health`
2. ヘルスチェックコマンドをコンテナ内で手動で実行してみる
3. アプリケーションが正しく設定されているか確認する
4. ヘルスチェックのパラメータ（interval, timeout, retries）を調整する

### 環境変数の問題

**問題**: 環境変数が正しく設定されていない、または読み込まれない。

**解決策**:
1. `.env`ファイルが正しい場所にあるか確認する
2. 環境変数の構文を確認する
3. コンテナ内の環境変数を確認する: `docker exec [container_name] env`
4. 必要に応じて環境変数を直接指定する: `docker-compose -f docker-compose.yml up -d`

これらのトラブルシューティング手順は、Docker Compose環境で発生する一般的な問題の解決に役立ちます。