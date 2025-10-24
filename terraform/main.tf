data "aws_caller_identity" "current" {}

# ECR（コンテナレジストリ）
# module "ecr" {
#   source = "./modules/ecr"
#
#   project_name            = var.project_name
#   repository_name         = "ecr-mental-app"
#   image_tag_mutability    = "IMMUTABLE"
#   scan_on_push            = false
#   encryption_type         = "AES256"
#   enable_lifecycle_policy = true
#   max_image_count         = 10
# }

# VPC（仮想ネットワーク）
module "vpc" {
  source = "./modules/vpc"

  project_name       = var.project_name
  region             = var.region
  vpc_cidr           = var.vpc_cidr
  public_subnet_cidr = "10.0.0.0/24"
}

# セキュリティグループ
module "security_group" {
  source = "./modules/security_group"

  project_name = var.project_name
  vpc_id       = module.vpc.vpc_id
}

# EFS（ファイルシステム）
module "efs" {
  source = "./modules/efs"

  project_name      = var.project_name
  subnet_id         = module.vpc.public_subnet_id
  security_group_id = module.security_group.efs_security_group_id
}

# ECS（コンテナサービス）
# module "ecs" {
#   source = "./modules/ecs"
#
#   project_name        = var.project_name
#   region              = var.region
#   ecr_repository_url  = module.ecr.repository_url
#   image_tag           = var.ecs_image_tag
#   task_cpu            = var.ecs_task_cpu
#   task_memory         = var.ecs_task_memory
#   desired_count       = var.ecs_desired_count
#   subnet_ids          = [module.vpc.public_subnet_id]
#   security_group_ids  = [module.security_group.lambda_security_group_id]
#   efs_file_system_id  = module.efs.efs_id
#   efs_file_system_arn = module.efs.efs_arn
#   efs_mount_target_id = module.efs.mount_target_id
#   database_path       = var.database_path
#   spring_active       = "ecs"
# }
#
# # ECS Scheduler（平日 08:30-20:00 JST に稼働）
# module "ecs_scheduler" {
#   source = "./modules/ecs_scheduler"
#
#   project_name   = var.project_name
#   cluster_arn    = module.ecs.cluster_arn
#   service_name   = module.ecs.service_name
#   start_schedule = "cron(30 23 ? * SUN-THU *)"
#   stop_schedule  = "cron(0 11 ? * MON-FRI *)"
# }

# EC2スポットインスタンス
module "ec2_spot" {
  source = "./modules/ec2_spot"

  project_name  = var.project_name
  instance_type = var.ec2_instance_type
  subnet_id     = module.vpc.public_subnet_id
  security_group_ids = [module.security_group.ec2_security_group_id]
  efs_id        = module.efs.efs_id
}

# EC2 Scheduler（毎日 08:30-20:00 JST に稼働）
module "ec2_scheduler" {
  source = "./modules/ec2_scheduler"

  project_name  = var.project_name
  instance_id   = module.ec2_spot.spot_instance_id
  start_schedule = "cron(30 23 ? * SUN-THU *)" # 08:30 JST (平日)
  stop_schedule = "cron(0 11 ? * * *)"        # 20:00 JST (毎日)
}
