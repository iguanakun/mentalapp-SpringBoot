# ビルドステージ
FROM maven:3.9-eclipse-temurin-21 AS build

# 作業ディレクトリの設定
WORKDIR /build

# Maven依存関係のキャッシュのため、ダウンロードのみを実行
COPY pom.xml .
RUN mvn dependency:go-offline

# Mavenビルドの実行
COPY src src
RUN mvn package

# 実行ステージ
FROM eclipse-temurin:21-jre

# 作業ディレクトリの設定
WORKDIR /app

# ビルドステージからJARファイルのみをコピー
COPY --from=build /build/target/*.jar app.jar

# ポートの公開
EXPOSE 8080

# JARファイルの実行
CMD ["java", "-jar", "app.jar"]