# output "ecr_repository_url" {
#   value = module.ecr.repository_url
# }
#
# output "ecs_cluster_name" {
#   value = module.ecs.cluster_name
# }
#
# output "ecs_service_name" {
#   value = module.ecs.service_name
# }
#
# output "ecs_task_definition_arn" {
#   value = module.ecs.task_definition_arn
# }

output "efs_id" {
  value = module.efs.efs_id
}

output "vpc_id" {
  value = module.vpc.vpc_id
}

output "ec2_spot_instance_id" {
  value = module.ec2_spot.spot_instance_id
}

output "ec2_public_ip" {
  value = module.ec2_spot.public_ip
}

output "elastic_ip" {
  value = module.ec2_spot.elastic_ip
}

output "ssh_command" {
  value = module.ec2_spot.ssh_command
}

# output "cloudfront_domain" {
#   value = module.cloudfront.distribution_domain_name
# }
#
# output "cloudfront_url" {
#   value = module.cloudfront.distribution_url
# }
