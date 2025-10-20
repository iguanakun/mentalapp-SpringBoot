variable "project_name" {
  description = "Project name"
  type        = string
}

variable "region" {
  description = "AWS region"
  type        = string
}

variable "ecr_repository_url" {
  description = "ECR repository URL"
  type        = string
}

variable "image_tag" {
  description = "Docker image tag"
  type        = string
}

variable "task_cpu" {
  description = "ECS task CPU units (512 = 0.5 vCPU)"
  type        = string
  default     = "512"
}

variable "task_memory" {
  description = "ECS task memory in MB"
  type        = string
  default     = "1024"
}

variable "container_name" {
  description = "Container name"
  type        = string
  default     = "app"
}

variable "container_port" {
  description = "Container port"
  type        = number
  default     = 8080
}

variable "desired_count" {
  description = "Desired number of ECS tasks"
  type        = number
  default     = 1
}

variable "subnet_ids" {
  description = "List of subnet IDs for ECS tasks"
  type = list(string)
}

variable "security_group_ids" {
  description = "List of security group IDs for ECS tasks"
  type = list(string)
}

variable "efs_file_system_id" {
  description = "EFS file system ID"
  type        = string
}

variable "efs_file_system_arn" {
  description = "EFS file system ARN"
  type        = string
}

variable "efs_mount_target_id" {
  description = "EFS mount target ID for dependency"
  type        = string
}

variable "database_path" {
  description = "Database path for SQLite on EFS"
  type        = string
  default     = "/mnt/efs/mentalapp.db"
}

variable "spring_active" {
  type = string
}
