output "function_name" {
  description = "Lambda function name"
  value       = aws_lambda_function.this.function_name
}

output "function_arn" {
  description = "Lambda function ARN"
  value       = aws_lambda_function.this.arn
}

output "function_url" {
  description = "Lambda Function URL"
  value       = aws_lambda_function_url.this.function_url
}

output "function_url_id" {
  description = "Lambda Function URL ID"
  value       = aws_lambda_function_url.this.url_id
}
