variable "project_name" {
  description = "Project name for resource naming"
  type        = string
}

variable "region" {
  description = "AWS region"
  type        = string
}

variable "account_id" {
  description = "AWS account ID"
  type        = string
}

variable "lambda_role_arn" {
  description = "Lambda execution role ARN"
  type        = string
}

variable "ecr_repository_url" {
  description = "ECR repository URL"
  type        = string
}

variable "image_tag" {
  description = "Lambda function image tag"
  type        = string
}

variable "timeout" {
  description = "Lambda function timeout in seconds"
  type        = number
  default     = 60
}

variable "memory_size" {
  description = "Lambda function memory size in MB"
  type        = number
  default     = 2048
}

variable "subnet_ids" {
  description = "VPC subnet IDs for Lambda"
  type = list(string)
}

variable "security_group_ids" {
  description = "Security group IDs for Lambda"
  type = list(string)
}

variable "efs_access_point_arn" {
  description = "EFS Access Point ARN"
  type        = string
}

variable "efs_mount_path" {
  description = "EFS local mount path"
  type        = string
  default     = "/mnt/efs"
}

variable "efs_mount_target_id" {
  description = "EFS Mount Target ID for dependency"
  type        = string
}

variable "environment_variables" {
  description = "Environment variables for Lambda"
  type = map(string)
  default = {}
}

variable "function_url_auth_type" {
  description = "Lambda Function URL authorization type"
  type        = string
  default     = "NONE"
}

variable "cors_allow_credentials" {
  description = "CORS allow credentials"
  type        = bool
  default     = false
}

variable "cors_allow_origins" {
  description = "CORS allowed origins"
  type = list(string)
  default = ["*"]
}

variable "cors_allow_methods" {
  description = "CORS allowed methods"
  type = list(string)
  default = ["*"]
}

variable "cors_allow_headers" {
  description = "CORS allowed headers"
  type = list(string)
  default = ["date", "keep-alive"]
}

variable "cors_expose_headers" {
  description = "CORS exposed headers"
  type = list(string)
  default = ["date", "keep-alive"]
}

variable "cors_max_age" {
  description = "CORS max age in seconds"
  type        = number
  default     = 86400
}
