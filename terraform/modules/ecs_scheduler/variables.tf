variable "project_name" {
  type        = string
}

variable "cluster_arn" {
  type        = string
}

variable "service_name" {
  type        = string
}

variable "start_schedule" {
  type        = string
  default     = "cron(30 23 ? * SUN-THU *)" # 08:30 JST (Mon-Fri)
}

variable "stop_schedule" {
  type        = string
  default     = "cron(0 11 ? * MON-FRI *)" # 20:00 JST (Mon-Fri)
}

variable "start_desired_count" {
  type        = number
  default     = 1
}

variable "stop_desired_count" {
  type        = number
  default     = 0
}
