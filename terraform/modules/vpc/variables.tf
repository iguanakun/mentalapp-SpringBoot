variable "project_name" {
  type        = string
}

variable "region" {
  type        = string
}

variable "vpc_cidr" {
  type        = string
}

variable "public_subnet_cidr" {
  type        = string
  default     = "10.0.0.0/24"
}

variable "private_subnet_cidr" {
  type        = string
  default     = "10.0.1.0/24"
}