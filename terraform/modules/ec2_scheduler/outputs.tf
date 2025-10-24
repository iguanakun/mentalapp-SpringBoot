output "scheduler_role_arn" {
  value       = aws_iam_role.ec2_scheduler_role.arn
}

output "start_schedule_arn" {
  value       = aws_scheduler_schedule.start_instance.arn
}

output "stop_schedule_arn" {
  value       = aws_scheduler_schedule.stop_instance.arn
}
