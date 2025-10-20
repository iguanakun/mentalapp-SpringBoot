output "scheduler_role_arn" {
  description = "IAM role ARN for EC2 scheduler"
  value       = aws_iam_role.ec2_scheduler_role.arn
}

output "start_schedule_arn" {
  description = "ARN of start schedule"
  value       = aws_scheduler_schedule.start_instance.arn
}

output "stop_schedule_arn" {
  description = "ARN of stop schedule"
  value       = aws_scheduler_schedule.stop_instance.arn
}
