variable "region" {
  type        = string
  default     = "ap-northeast-1"
}

variable "project_name" {
  type        = string
  default     = "mentalapp"
}

variable "vpc_cidr" {
  type        = string
  default     = "10.0.0.0/16"
}

variable "ecs_image_tag" {
  type        = string
  default = "latest"
}

variable "ecs_task_cpu" {
  type        = string
  default     = "512"
}

variable "ecs_task_memory" {
  type        = string
  default     = "1024"
}

variable "ecs_desired_count" {
  type        = number
  default     = 1
}

variable "database_path" {
  type        = string
  default     = "/mnt/efs/mentalapp.db"
}

variable "ec2_instance_type" {
  type        = string
  default     = "t3.micro"
}
