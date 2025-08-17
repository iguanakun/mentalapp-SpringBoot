# ビルドステージ
FROM maven:3.9-eclipse-temurin-22 AS build

# 作業ディレクトリの設定
WORKDIR /build

# 依存関係のキャッシュを最適化するため、pom.xmlを先にコピー
COPY pom.xml .

# Maven依存関係のキャッシュのため、ダウンロードのみを実行
RUN mvn dependency:go-offline

# ソースコードのコピー
COPY src src

# Mavenビルドの実行
# テストはスキップしてパッケージングのみを実行
RUN mvn package -DskipTests

# 実行ステージ
FROM eclipse-temurin:22-jre

# 作業ディレクトリの設定
WORKDIR /app

# ビルドステージからJARファイルのみをコピー
COPY --from=build /build/target/*.jar app.jar

# ポートの公開（環境変数を使用）
EXPOSE 8080

# JARファイルの実行
CMD ["java", "-jar", "app.jar"]