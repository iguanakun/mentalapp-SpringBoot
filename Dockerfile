# ベースイメージの指定
FROM maven:3.9-eclipse-temurin-22

# 作業ディレクトリの設定
WORKDIR /app

# ビルドに必要なファイルのコピー
COPY pom.xml .
COPY src src

# Mavenビルドの実行
RUN mvn clean package

# ポートの公開
EXPOSE 8080

COPY target target

# JARファイルの実行
CMD ["java", "-jar", "target/application.jar"]