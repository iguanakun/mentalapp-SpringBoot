variable "project_name" {
  description = "Project name for resource naming"
  type        = string
}

variable "instance_id" {
  description = "EC2 instance ID to schedule"
  type        = string
}

variable "start_schedule" {
  description = "Cron expression for starting instance (UTC)"
  type        = string
  default     = "cron(30 23 ? * SUN-THU *)" # 08:30 JST (Mon-Fri)
}

variable "stop_schedule" {
  description = "Cron expression for stopping instance (UTC)"
  type        = string
  default     = "cron(0 11 ? * MON-FRI *)" # 20:00 JST (Mon-Fri)
}
