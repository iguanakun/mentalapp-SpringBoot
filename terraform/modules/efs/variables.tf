variable "project_name" {
  description = "Project name for resource naming"
  type        = string
}

variable "subnet_id" {
  description = "Subnet ID for EFS mount target"
  type        = string
}

variable "security_group_id" {
  description = "Security Group ID for EFS"
  type        = string
}
