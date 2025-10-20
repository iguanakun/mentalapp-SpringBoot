# CloudFront Cache Policy
resource "aws_cloudfront_cache_policy" "this" {
  name        = "${var.project_name}-cache-policy"
  comment     = "Cache policy for ${var.project_name}"
  default_ttl = var.cache_default_ttl
  max_ttl     = var.cache_max_ttl
  min_ttl     = var.cache_min_ttl

  parameters_in_cache_key_and_forwarded_to_origin {
    cookies_config {
      cookie_behavior = var.cookies_behavior
    }

    headers_config {
      header_behavior = var.headers_behavior
    }

    query_strings_config {
      query_string_behavior = var.query_strings_behavior
    }

    enable_accept_encoding_gzip   = var.enable_gzip
    enable_accept_encoding_brotli = var.enable_brotli
  }
}

# CloudFront Origin Request Policy
resource "aws_cloudfront_origin_request_policy" "this" {
  name    = "${var.project_name}-origin-policy"
  comment = "Origin request policy for ${var.project_name}"

  cookies_config {
    cookie_behavior = var.origin_cookies_behavior
  }

  headers_config {
    header_behavior = var.origin_headers_behavior
  }

  query_strings_config {
    query_string_behavior = var.origin_query_strings_behavior
  }
}

# CloudFront Distribution
resource "aws_cloudfront_distribution" "this" {
  origin {
    domain_name = var.origin_domain_name
    origin_id   = "${var.project_name}-lambda"

    custom_origin_config {
      http_port              = var.origin_http_port
      https_port             = var.origin_https_port
      origin_protocol_policy = var.origin_protocol_policy
      origin_ssl_protocols   = var.origin_ssl_protocols
    }
  }

  enabled = var.enabled

  default_cache_behavior {
    allowed_methods          = var.allowed_methods
    cached_methods           = var.cached_methods
    target_origin_id         = "${var.project_name}-lambda"
    compress                 = var.compress
    viewer_protocol_policy   = var.viewer_protocol_policy
    cache_policy_id          = aws_cloudfront_cache_policy.this.id
    origin_request_policy_id = aws_cloudfront_origin_request_policy.this.id
  }

  restrictions {
    geo_restriction {
      restriction_type = var.geo_restriction_type
    }
  }

  viewer_certificate {
    cloudfront_default_certificate = var.use_default_certificate
  }

  tags = {
    Name = "${var.project_name}-cdn"
  }
}
