variable "region" {
  description = "AWS region"
  type        = string
  default     = "ap-northeast-1"
}

variable "project_name" {
  description = "Project name"
  type        = string
  default     = "mentalapp"
}

variable "vpc_cidr" {
  description = "VPC CIDR block"
  type        = string
  default     = "10.0.0.0/16"
}

variable "ecs_image_tag" {
  description = "ECS task image tag"
  type        = string
  default = "latest"
}

variable "ecs_task_cpu" {
  description = "ECS task CPU units (512 = 0.5 vCPU)"
  type        = string
  default     = "512"
}

variable "ecs_task_memory" {
  description = "ECS task memory in MB"
  type        = string
  default     = "1024"
}

variable "ecs_desired_count" {
  description = "Desired number of ECS tasks"
  type        = number
  default     = 1
}

variable "database_path" {
  description = "Database path for SQLite on EFS"
  type        = string
  default     = "/mnt/efs/mentalapp.db"
}
