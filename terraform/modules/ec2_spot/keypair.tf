# SSH キーペア用のTLSプライベートキー生成
resource "tls_private_key" "ec2_key" {
  algorithm = "RSA"
  rsa_bits  = 4096
}

# AWS EC2キーペア
resource "aws_key_pair" "ec2_key_pair" {
  key_name   = "${var.project_name}-ec2-key"
  public_key = tls_private_key.ec2_key.public_key_openssh

  tags = {
    Name    = "${var.project_name}-ec2-key"
    Project = var.project_name
  }
}

# プライベートキーをローカルに保存
resource "local_file" "private_key" {
  content         = tls_private_key.ec2_key.private_key_pem
  filename        = "${path.root}/../deploy/${var.project_name}-ec2-key.pem"
  file_permission = "0600"
}
