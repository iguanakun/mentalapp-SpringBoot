variable "project_name" {
  description = "Project name for resource naming"
  type        = string
}

variable "efs_access_point_arn" {
  description = "EFS Access Point ARN for IAM policy"
  type        = string
}
