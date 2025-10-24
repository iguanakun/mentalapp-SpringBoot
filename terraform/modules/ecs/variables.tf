variable "project_name" {
  type        = string
}

variable "region" {
  type        = string
}

variable "ecr_repository_url" {
  type        = string
}

variable "image_tag" {
  type        = string
}

variable "task_cpu" {
  type        = string
  default     = "512"
}

variable "task_memory" {
  type        = string
  default     = "1024"
}

variable "container_name" {
  type        = string
  default     = "app"
}

variable "container_port" {
  type        = number
  default     = 8080
}

variable "desired_count" {
  type        = number
  default     = 1
}

variable "subnet_ids" {
  type = list(string)
}

variable "security_group_ids" {
  type = list(string)
}

variable "efs_file_system_id" {
  type        = string
}

variable "efs_file_system_arn" {
  type        = string
}

variable "efs_mount_target_id" {
  type        = string
}

variable "database_path" {
  type        = string
  default     = "/mnt/efs/mentalapp.db"
}

variable "spring_active" {
  type = string
}
