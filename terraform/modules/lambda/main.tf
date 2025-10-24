# Lambda関数
resource "aws_lambda_function" "this" {
  function_name = var.project_name
  role          = var.lambda_role_arn
  package_type  = "Image"
  image_uri     = "${var.ecr_repository_url}:${var.image_tag}"

  timeout     = var.timeout
  memory_size = var.memory_size

  vpc_config {
    subnet_ids         = var.subnet_ids
    security_group_ids = var.security_group_ids
  }

  file_system_config {
    arn              = var.efs_access_point_arn
    local_mount_path = var.efs_mount_path
  }

  environment {
    variables = var.environment_variables
  }

  depends_on = [
    var.efs_mount_target_id
  ]
}

# Lambda関数URL
resource "aws_lambda_function_url" "this" {
  function_name      = aws_lambda_function.this.function_name
  authorization_type = var.function_url_auth_type

  cors {
    allow_credentials = var.cors_allow_credentials
    allow_origins     = var.cors_allow_origins
    allow_methods     = var.cors_allow_methods
    allow_headers     = var.cors_allow_headers
    expose_headers    = var.cors_expose_headers
    max_age           = var.cors_max_age
  }
}
