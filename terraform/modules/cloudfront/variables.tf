variable "project_name" {
  description = "Project name for resource naming"
  type        = string
}

variable "origin_domain_name" {
  description = "Origin domain name (Lambda Function URL without https://)"
  type        = string
}

# Cache Policy Variables
variable "cache_default_ttl" {
  description = "Default TTL for cache"
  type        = number
  default     = 0
}

variable "cache_max_ttl" {
  description = "Maximum TTL for cache"
  type        = number
  default     = 0
}

variable "cache_min_ttl" {
  description = "Minimum TTL for cache"
  type        = number
  default     = 0
}

variable "cookies_behavior" {
  description = "Cookie behavior for cache key"
  type        = string
  default     = "all"
}

variable "headers_behavior" {
  description = "Header behavior for cache key"
  type        = string
  default     = "none"
}

variable "query_strings_behavior" {
  description = "Query string behavior for cache key"
  type        = string
  default     = "all"
}

variable "enable_gzip" {
  description = "Enable gzip encoding"
  type        = bool
  default     = true
}

variable "enable_brotli" {
  description = "Enable brotli encoding"
  type        = bool
  default     = true
}

# Origin Request Policy Variables
variable "origin_cookies_behavior" {
  description = "Cookie behavior for origin request"
  type        = string
  default     = "all"
}

variable "origin_headers_behavior" {
  description = "Header behavior for origin request"
  type        = string
  default     = "allViewer"
}

variable "origin_query_strings_behavior" {
  description = "Query string behavior for origin request"
  type        = string
  default     = "all"
}

# Distribution Variables
variable "origin_http_port" {
  description = "Origin HTTP port"
  type        = number
  default     = 443
}

variable "origin_https_port" {
  description = "Origin HTTPS port"
  type        = number
  default     = 443
}

variable "origin_protocol_policy" {
  description = "Origin protocol policy"
  type        = string
  default     = "https-only"
}

variable "origin_ssl_protocols" {
  description = "Origin SSL protocols"
  type        = list(string)
  default     = ["TLSv1.2"]
}

variable "enabled" {
  description = "Enable CloudFront distribution"
  type        = bool
  default     = true
}

variable "allowed_methods" {
  description = "Allowed HTTP methods"
  type        = list(string)
  default     = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
}

variable "cached_methods" {
  description = "Cached HTTP methods"
  type        = list(string)
  default     = ["GET", "HEAD"]
}

variable "compress" {
  description = "Enable compression"
  type        = bool
  default     = true
}

variable "viewer_protocol_policy" {
  description = "Viewer protocol policy"
  type        = string
  default     = "redirect-to-https"
}

variable "geo_restriction_type" {
  description = "Geo restriction type"
  type        = string
  default     = "none"
}

variable "use_default_certificate" {
  description = "Use default CloudFront certificate"
  type        = bool
  default     = true
}
