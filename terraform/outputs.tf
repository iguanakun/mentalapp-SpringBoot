# ECR出力
# output "ecr_repository_url" {
#   description = "ECR Repository URL"
#   value       = module.ecr.repository_url
# }
#
# # ECS出力
# output "ecs_cluster_name" {
#   description = "ECS Cluster Name"
#   value       = module.ecs.cluster_name
# }
#
# output "ecs_service_name" {
#   description = "ECS Service Name"
#   value       = module.ecs.service_name
# }
#
# output "ecs_task_definition_arn" {
#   description = "ECS Task Definition ARN"
#   value       = module.ecs.task_definition_arn
# }

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

# EC2出力
output "ec2_spot_instance_id" {
  description = "EC2 Spot Instance ID"
  value       = module.ec2_spot.spot_instance_id
}

output "ec2_public_ip" {
  description = "EC2 Public IP Address"
  value       = module.ec2_spot.public_ip
}

# CloudFront出力
# output "cloudfront_domain" {
#   description = "CloudFront Distribution Domain"
#   value       = module.cloudfront.distribution_domain_name
# }
#
# output "cloudfront_url" {
#   description = "CloudFront Distribution URL"
#   value       = module.cloudfront.distribution_url
# }
