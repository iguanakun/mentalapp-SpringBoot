#!/bin/bash
set -e

# ログ設定
exec > >(tee /var/log/user-data.log)
exec 2>&1

echo "Starting user data script..."

# システムアップデート
yum update -y

# Java 21インストール
echo "Installing Amazon Corretto 21..."
rpm --import https://yum.corretto.aws/corretto.key
curl -L -o /etc/yum.repos.d/corretto.repo https://yum.corretto.aws/corretto.repo
yum install -y java-21-amazon-corretto-devel

# EFSマウントポイント作成
echo "Creating EFS mount point..."
mkdir -p /mnt/efs

# EFS utils インストール
echo "Installing amazon-efs-utils..."
yum install -y amazon-efs-utils

# /etc/fstab に EFS マウント設定を追加
if ! grep -q "${EFS_ID}" /etc/fstab; then
    echo "Adding EFS mount configuration to /etc/fstab..."
    echo "${EFS_ID}:/ /mnt/efs efs _netdev,tls 0 0" >> /etc/fstab
fi

# EFSマウント
echo "Mounting EFS..."
mount -a -t efs defaults

# EFSマウントポイントの所有者変更
chown -R ec2-user:ec2-user /mnt/efs

# アプリケーションディレクトリ作成
mkdir -p /opt/mentalapp
cd /opt/mentalapp

# systemdサービスファイル作成
cat <<'SERVICE' > /etc/systemd/system/mentalapp.service
[Unit]
Description=MentalApp Spring Boot Application
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/opt/mentalapp
Environment="SPRING_PROFILES_ACTIVE=prd"
ExecStart=/usr/bin/java -jar /opt/mentalapp/mentalapp-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal
SyslogIdentifier=mentalapp

[Install]
WantedBy=multi-user.target
SERVICE

# サービスの権限設定
chmod 644 /etc/systemd/system/mentalapp.service
chown -R ec2-user:ec2-user /opt/mentalapp

# systemdリロード
systemctl daemon-reload