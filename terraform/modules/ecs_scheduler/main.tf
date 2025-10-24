# EventBridgeスケジューラ用IAMロール
resource "aws_iam_role" "scheduler_role" {
  name = "${var.project_name}-ecs-scheduler-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "scheduler.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })

  tags = {
    Name    = "${var.project_name}-ecs-scheduler-role"
    Project = var.project_name
  }
}

# ECSサービス更新用IAMポリシー
resource "aws_iam_role_policy" "scheduler_policy" {
  name = "${var.project_name}-ecs-scheduler-policy"
  role = aws_iam_role.scheduler_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ecs:UpdateService",
          "ecs:DescribeServices"
        ]
        Resource = "*"
      }
    ]
  })
}

# EventBridgeスケジューラグループ
resource "aws_scheduler_schedule_group" "ecs_scheduler_group" {
  name = "${var.project_name}-ecs-scheduler-group"

  tags = {
    Name    = "${var.project_name}-ecs-scheduler-group"
    Project = var.project_name
  }
}

# EventBridgeスケジュール - ECSサービス起動（平日08:30 JST）
resource "aws_scheduler_schedule" "start_service" {
  name       = "${var.project_name}-ecs-start-schedule"
  group_name = aws_scheduler_schedule_group.ecs_scheduler_group.name

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression          = var.start_schedule
  schedule_expression_timezone = "UTC"

  target {
    arn      = "arn:aws:scheduler:::aws-sdk:ecs:updateService"
    role_arn = aws_iam_role.scheduler_role.arn

    input = jsonencode({
      Cluster      = var.cluster_arn
      Service      = var.service_name
      DesiredCount = var.start_desired_count
    })
  }

  description = "Start ECS service on weekdays at 08:30 JST (23:30 UTC previous day)"
}

# EventBridgeスケジュール - ECSサービス停止（平日20:00 JST）
resource "aws_scheduler_schedule" "stop_service" {
  name       = "${var.project_name}-ecs-stop-schedule"
  group_name = aws_scheduler_schedule_group.ecs_scheduler_group.name

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression          = var.stop_schedule
  schedule_expression_timezone = "UTC"

  target {
    arn      = "arn:aws:scheduler:::aws-sdk:ecs:updateService"
    role_arn = aws_iam_role.scheduler_role.arn

    input = jsonencode({
      Cluster      = var.cluster_arn
      Service      = var.service_name
      DesiredCount = var.stop_desired_count
    })
  }

  description = "Stop ECS service on weekdays at 20:00 JST (11:00 UTC)"
}
