output "cluster_id" {
  value       = aws_ecs_cluster.main.id
}

output "cluster_arn" {
  value       = aws_ecs_cluster.main.arn
}

output "cluster_name" {
  value       = aws_ecs_cluster.main.name
}

output "service_id" {
  value       = aws_ecs_service.app.id
}

output "service_name" {
  value       = aws_ecs_service.app.name
}

output "task_definition_arn" {
  value       = aws_ecs_task_definition.app.arn
}

output "task_execution_role_arn" {
  value       = aws_iam_role.ecs_task_execution_role.arn
}

output "task_role_arn" {
  value       = aws_iam_role.ecs_task_role.arn
}

output "cloudwatch_log_group_name" {
  value       = aws_cloudwatch_log_group.ecs.name
}
