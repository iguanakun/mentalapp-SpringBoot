variable "project_name" {
  description = "Project name for resource naming"
  type        = string
}

variable "vpc_id" {
  description = "VPC ID"
  type        = string
}

variable "public_subnet_id" {
  description = "Public subnet ID for NAT Gateway placement"
  type        = string
}

variable "private_subnet_id" {
  description = "Private subnet ID for route table association"
  type        = string
}

variable "internet_gateway_id" {
  description = "Internet Gateway ID for dependency"
  type        = string
}

variable "availability_zone_suffix" {
  description = "Availability zone suffix for naming (e.g., 1a, 1c)"
  type        = string
  default     = "1a"
}
