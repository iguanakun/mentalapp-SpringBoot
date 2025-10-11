output "efs_id" {
  description = "EFS File System ID"
  value       = aws_efs_file_system.this.id
}

output "efs_arn" {
  description = "EFS File System ARN"
  value       = aws_efs_file_system.this.arn
}

output "mount_target_id" {
  description = "EFS Mount Target ID"
  value       = aws_efs_mount_target.this.id
}
