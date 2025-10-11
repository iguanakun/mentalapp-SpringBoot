output "efs_security_group_id" {
  description = "EFS Security Group ID"
  value       = aws_security_group.efs.id
}

output "lambda_security_group_id" {
  description = "Lambda Security Group ID"
  value       = aws_security_group.lambda.id
}
