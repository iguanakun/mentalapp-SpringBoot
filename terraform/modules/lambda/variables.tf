variable "project_name" {
  type        = string
}

variable "lambda_role_arn" {
  type        = string
}

variable "ecr_repository_url" {
  type        = string
}

variable "image_tag" {
  type        = string
}

variable "timeout" {
  type        = number
  default     = 60
}

variable "memory_size" {
  type        = number
  default     = 2048
}

variable "subnet_ids" {
  type = list(string)
}

variable "security_group_ids" {
  type = list(string)
}

variable "efs_access_point_arn" {
  type        = string
}

variable "efs_mount_path" {
  type        = string
  default     = "/mnt/efs"
}

variable "efs_mount_target_id" {
  type        = string
}

variable "environment_variables" {
  type = map(string)
  default = {}
}

variable "function_url_auth_type" {
  type        = string
  default     = "NONE"
}

variable "cors_allow_credentials" {
  type        = bool
  default     = false
}

variable "cors_allow_origins" {
  type = list(string)
  default = ["*"]
}

variable "cors_allow_methods" {
  type = list(string)
  default = ["*"]
}

variable "cors_allow_headers" {
  type = list(string)
  default = ["date", "keep-alive"]
}

variable "cors_expose_headers" {
  type = list(string)
  default = ["date", "keep-alive"]
}

variable "cors_max_age" {
  type        = number
  default     = 86400
}
