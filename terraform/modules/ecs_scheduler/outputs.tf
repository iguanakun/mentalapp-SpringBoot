output "scheduler_role_arn" {
  description = "ARN of the EventBridge Scheduler IAM role"
  value       = aws_iam_role.scheduler_role.arn
}

output "scheduler_group_name" {
  description = "Name of the EventBridge Scheduler group"
  value       = aws_scheduler_schedule_group.ecs_scheduler_group.name
}

output "start_schedule_arn" {
  description = "ARN of the start schedule"
  value       = aws_scheduler_schedule.start_service.arn
}

output "stop_schedule_arn" {
  description = "ARN of the stop schedule"
  value       = aws_scheduler_schedule.stop_service.arn
}

output "start_schedule_name" {
  description = "Name of the start schedule"
  value       = aws_scheduler_schedule.start_service.name
}

output "stop_schedule_name" {
  description = "Name of the stop schedule"
  value       = aws_scheduler_schedule.stop_service.name
}
