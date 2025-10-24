output "efs_id" {
  value       = aws_efs_file_system.this.id
}

output "efs_arn" {
  value       = aws_efs_file_system.this.arn
}

output "mount_target_id" {
  value       = aws_efs_mount_target.this.id
}
