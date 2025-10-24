# Amazon Linux 2のAMIを取得
data "aws_ami" "amazon_linux_2" {
  most_recent = true
  owners = ["amazon"]

  filter {
    name = "name"
    values = ["amzn2-ami-hvm-*-x86_64-gp2"]
  }

  filter {
    name = "virtualization-type"
    values = ["hvm"]
  }
}

# EC2インスタンス用IAMロール
resource "aws_iam_role" "ec2_role" {
  name = "${var.project_name}-ec2-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })

  tags = {
    Name    = "${var.project_name}-ec2-role"
    Project = var.project_name
  }
}

resource "aws_iam_role_policy_attachment" "ssm_policy" {
  role       = aws_iam_role.ec2_role.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}

# インスタンスプロファイル
resource "aws_iam_instance_profile" "ec2_profile" {
  name = "${var.project_name}-ec2-profile"
  role = aws_iam_role.ec2_role.name

  tags = {
    Name    = "${var.project_name}-ec2-profile"
    Project = var.project_name
  }
}

# ユーザーデータスクリプト
locals {
  user_data = templatefile("${path.root}/../deploy/scripts.sh", {
    EFS_ID = var.efs_id
  })
}

# EC2スポットインスタンス
resource "aws_spot_instance_request" "mentalapp" {
  ami                    = data.aws_ami.amazon_linux_2.id
  instance_type          = var.instance_type
  subnet_id              = var.subnet_id
  vpc_security_group_ids = var.security_group_ids
  iam_instance_profile   = aws_iam_instance_profile.ec2_profile.name
  user_data              = local.user_data
  key_name = aws_key_pair.ec2_key_pair.key_name

  spot_type                      = "persistent"
  instance_interruption_behavior = "stop"
  wait_for_fulfillment = true

  associate_public_ip_address = true

  root_block_device {
    volume_type           = "gp3"
    volume_size           = 8
    delete_on_termination = true
  }

  tags = {
    Name    = "${var.project_name}-spot-instance"
    Project = var.project_name
  }

  # スポットインスタンスの再作成を防ぐ
  lifecycle {
    ignore_changes = [
      spot_instance_id,
      ami,
      user_data
    ]
  }
}

# スポットインスタンスにタグを付与
resource "aws_ec2_tag" "spot_instance_name" {
  resource_id = aws_spot_instance_request.mentalapp.spot_instance_id
  key         = "Name"
  value       = "${var.project_name}-spot-instance"
}

resource "aws_ec2_tag" "spot_instance_project" {
  resource_id = aws_spot_instance_request.mentalapp.spot_instance_id
  key         = "Project"
  value       = var.project_name
}

# Elastic IPの作成
resource "aws_eip" "mentalapp" {
  domain = "vpc"

  tags = {
    Name    = "${var.project_name}-eip"
    Project = var.project_name
  }
}

# Elastic IPをEC2インスタンスに関連付け
resource "aws_eip_association" "mentalapp" {
  instance_id   = aws_spot_instance_request.mentalapp.spot_instance_id
  allocation_id = aws_eip.mentalapp.id
}
