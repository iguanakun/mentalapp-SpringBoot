# IAM Role for EventBridge Scheduler
resource "aws_iam_role" "ec2_scheduler_role" {
  name = "${var.project_name}-ec2-scheduler-role"

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
    Name    = "${var.project_name}-ec2-scheduler-role"
    Project = var.project_name
  }
}

# IAM Policy for EC2 Start/Stop
resource "aws_iam_role_policy" "ec2_scheduler_policy" {
  name = "${var.project_name}-ec2-scheduler-policy"
  role = aws_iam_role.ec2_scheduler_role.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "ec2:StartInstances",
          "ec2:StopInstances",
          "ec2:DescribeInstances"
        ]
        Resource = "*"
      }
    ]
  })
}

# EventBridge Scheduler Group
resource "aws_scheduler_schedule_group" "ec2_scheduler_group" {
  name = "${var.project_name}-ec2-scheduler-group"

  tags = {
    Name    = "${var.project_name}-ec2-scheduler-group"
    Project = var.project_name
  }
}

# EventBridge Schedule - Start EC2 Instance (Weekdays 08:30 JST)
resource "aws_scheduler_schedule" "start_instance" {
  name       = "${var.project_name}-ec2-start-schedule"
  group_name = aws_scheduler_schedule_group.ec2_scheduler_group.name

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression          = var.start_schedule
  schedule_expression_timezone = "UTC"

  target {
    arn      = "arn:aws:scheduler:::aws-sdk:ec2:startInstances"
    role_arn = aws_iam_role.ec2_scheduler_role.arn

    input = jsonencode({
      InstanceIds = [var.instance_id]
    })
  }

  description = "Start EC2 instance on weekdays at 08:30 JST (23:30 UTC previous day)"
}

# EventBridge Schedule - Stop EC2 Instance (Weekdays 20:00 JST)
resource "aws_scheduler_schedule" "stop_instance" {
  name       = "${var.project_name}-ec2-stop-schedule"
  group_name = aws_scheduler_schedule_group.ec2_scheduler_group.name

  flexible_time_window {
    mode = "OFF"
  }

  schedule_expression          = var.stop_schedule
  schedule_expression_timezone = "UTC"

  target {
    arn      = "arn:aws:scheduler:::aws-sdk:ec2:stopInstances"
    role_arn = aws_iam_role.ec2_scheduler_role.arn

    input = jsonencode({
      InstanceIds = [var.instance_id]
    })
  }

  description = "Stop EC2 instance on weekdays at 20:00 JST (11:00 UTC)"
}
