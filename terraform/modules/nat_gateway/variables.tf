variable "project_name" {
  type        = string
}

variable "vpc_id" {
  type        = string
}

variable "public_subnet_id" {
  type        = string
}

variable "private_subnet_id" {
  type        = string
}

variable "internet_gateway_id" {
  type        = string
}

variable "availability_zone_suffix" {
  type        = string
  default     = "1a"
}
