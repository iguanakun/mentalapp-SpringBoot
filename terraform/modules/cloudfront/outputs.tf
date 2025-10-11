output "distribution_id" {
  description = "CloudFront Distribution ID"
  value       = aws_cloudfront_distribution.this.id
}

output "distribution_arn" {
  description = "CloudFront Distribution ARN"
  value       = aws_cloudfront_distribution.this.arn
}

output "distribution_domain_name" {
  description = "CloudFront Distribution Domain Name"
  value       = aws_cloudfront_distribution.this.domain_name
}

output "distribution_url" {
  description = "CloudFront Distribution URL"
  value       = "https://${aws_cloudfront_distribution.this.domain_name}"
}
