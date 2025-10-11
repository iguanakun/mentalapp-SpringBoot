# ECR出力
output "ecr_repository_url" {
  description = "ECR Repository URL"
  value       = module.ecr.repository_url
}

# ECS出力
output "ecs_cluster_name" {
  description = "ECS Cluster Name"
  value       = module.ecs.cluster_name
}

output "ecs_service_name" {
  description = "ECS Service Name"
  value       = module.ecs.service_name
}

output "ecs_task_definition_arn" {
  description = "ECS Task Definition ARN"
  value       = module.ecs.task_definition_arn
}

# EFS出力
output "efs_id" {
  description = "EFS File System ID"
  value       = module.efs.efs_id
}

# VPC出力
output "vpc_id" {
  description = "VPC ID"
  value       = module.vpc.vpc_id
}

# 将来の拡張用にコメントアウト
# output "private_subnet_id" {
#   description = "Private Subnet ID"
#   value       = module.vpc.private_subnet_id
# }

# CloudFront出力 (コメントアウト)
# output "cloudfront_domain" {
#   description = "CloudFront Distribution Domain"
#   value       = module.cloudfront.distribution_domain_name
# }
#
# output "cloudfront_url" {
#   description = "CloudFront Distribution URL"
#   value       = module.cloudfront.distribution_url
# }
