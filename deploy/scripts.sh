#!/bin/bash
set -e

yum update -y

# Javaインストール
rpm --import https://yum.corretto.aws/corretto.key
curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo
yum install -y java-21-amazon-corretto-devel

# EFSマウントポイント作成
mkdir -p /mnt/efs

# EFS utilsインストール(TLS暗号化通信に必要)
yum install -y amazon-efs-utils

# /etc/fstabにEFSマウント設定を追加(再起動時も自動マウント)
# ${EFS_ID}はTerraformから渡される変数
if ! grep -q "${EFS_ID}" /etc/fstab; then
    echo "${EFS_ID}:/ /mnt/efs efs _netdev,tls 0 0" >> /etc/fstab
fi

# EFSマウント
mount -a -t efs defaults

# 権限設定(SQLiteデータベースアクセスに必要)
chown -R ec2-user:ec2-user /mnt/efs

mkdir -p /opt/mentalapp
cd /opt/mentalapp

# Spring Bootを自動起動するための設定
cat <<'SERVICE' > /etc/systemd/system/mentalapp.service
[Unit]
Description=Mental App Spring Boot Application
# ネットワークが起動してから実行(EFS接続に必要)
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/mentalapp
# 環境変数(application-prd.propertiesを使用)
Environment="SPRING_PROFILES_ACTIVE=prd"
# jar自動起動
ExecStart=/usr/bin/java -jar /opt/mentalapp/mentalapp-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=mentalapp

[Install]
WantedBy=multi-user.target
SERVICE

# サービスファイルの権限設定
chmod 644 /etc/systemd/system/mentalapp.service
chown -R ec2-user:ec2-user /opt/mentalapp

# systemd設定の再読み込み
systemctl daemon-reload
