# EFS作成
resource "aws_efs_file_system" "this" {
  creation_token = "${var.project_name}-efs"
  encrypted      = true

  tags = {
    Name = "${var.project_name}-efs"
  }
}

# EFSマウントターゲット
resource "aws_efs_mount_target" "this" {
  file_system_id = aws_efs_file_system.this.id
  subnet_id      = var.subnet_id
  security_groups = [var.security_group_id]
}
