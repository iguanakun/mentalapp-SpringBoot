output "lambda_role_arn" {
  description = "Lambda Execution Role ARN"
  value       = aws_iam_role.lambda_role.arn
}

output "lambda_role_name" {
  description = "Lambda Execution Role Name"
  value       = aws_iam_role.lambda_role.name
}
