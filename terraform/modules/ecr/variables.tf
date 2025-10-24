variable "project_name" {
  type        = string
}

variable "repository_name" {
  type        = string
}

variable "image_tag_mutability" {
  type        = string
  default     = "IMMUTABLE"
}

variable "scan_on_push" {
  type        = bool
  default     = false
}

variable "encryption_type" {
  type        = string
  default     = "AES256"
}

variable "enable_lifecycle_policy" {
  type        = bool
  default     = true
}

variable "max_image_count" {
  type        = number
  default     = 10
}
