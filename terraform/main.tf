# データソース
data "aws_caller_identity" "current" {}

# ECRモジュール
module "ecr" {
  source = "./modules/ecr"

  project_name            = var.project_name
  repository_name         = "ecr-mental-app"
  image_tag_mutability    = "IMMUTABLE"
  scan_on_push            = false
  encryption_type         = "AES256"
  enable_lifecycle_policy = true
  max_image_count         = 10
}

# VPCモジュール
module "vpc" {
  source = "./modules/vpc"

  project_name       = var.project_name
  region             = var.region
  vpc_cidr           = var.vpc_cidr
  public_subnet_cidr = "10.0.0.0/24"
}

# セキュリティグループモジュール
module "security_group" {
  source = "./modules/security_group"

  project_name = var.project_name
  vpc_id       = module.vpc.vpc_id
}

# EFSモジュール
module "efs" {
  source = "./modules/efs"

  project_name      = var.project_name
  subnet_id         = module.vpc.public_subnet_id
  security_group_id = module.security_group.efs_security_group_id
}

# ECSモジュール
module "ecs" {
  source = "./modules/ecs"

  project_name        = var.project_name
  region              = var.region
  ecr_repository_url  = module.ecr.repository_url
  image_tag           = var.ecs_image_tag
  task_cpu            = var.ecs_task_cpu
  task_memory         = var.ecs_task_memory
  desired_count       = var.ecs_desired_count
  subnet_ids = [module.vpc.public_subnet_id]
  security_group_ids = [module.security_group.lambda_security_group_id]
  efs_file_system_id  = module.efs.efs_id
  efs_file_system_arn = module.efs.efs_arn
  efs_mount_target_id = module.efs.mount_target_id
  database_path       = var.database_path
}
