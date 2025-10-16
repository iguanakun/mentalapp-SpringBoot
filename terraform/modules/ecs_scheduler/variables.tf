variable "project_name" {
  description = "Project name for resource naming"
  type        = string
}

variable "cluster_arn" {
  description = "ECS cluster ARN"
  type        = string
}

variable "service_name" {
  description = "ECS service name"
  type        = string
}

variable "start_schedule" {
  description = "Cron expression for starting ECS service (UTC timezone)"
  type        = string
  default     = "cron(30 23 ? * SUN-THU *)" # 08:30 JST (Mon-Fri)
}

variable "stop_schedule" {
  description = "Cron expression for stopping ECS service (UTC timezone)"
  type        = string
  default     = "cron(0 11 ? * MON-FRI *)" # 20:00 JST (Mon-Fri)
}

variable "start_desired_count" {
  description = "Desired count when starting the service"
  type        = number
  default     = 1
}

variable "stop_desired_count" {
  description = "Desired count when stopping the service"
  type        = number
  default     = 0
}
