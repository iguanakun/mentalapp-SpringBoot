variable "project_name" {
  type = string
}

variable "origin_domain_name" {
  type = string
}

variable "cache_default_ttl" {
  type    = number
  default = 0
}

variable "cache_max_ttl" {
  type    = number
  default = 0
}

variable "cache_min_ttl" {
  type    = number
  default = 0
}

variable "cookies_behavior" {
  type    = string
  default = "all"
}

variable "headers_behavior" {
  type    = string
  default = "none"
}

variable "query_strings_behavior" {
  type    = string
  default = "all"
}

variable "enable_gzip" {
  type    = bool
  default = true
}

variable "enable_brotli" {
  type    = bool
  default = true
}

variable "origin_cookies_behavior" {
  type    = string
  default = "all"
}

variable "origin_headers_behavior" {
  type    = string
  default = "allViewer"
}

variable "origin_query_strings_behavior" {
  type    = string
  default = "all"
}

variable "origin_http_port" {
  type    = number
  default = 443
}

variable "origin_https_port" {
  type    = number
  default = 443
}

variable "origin_protocol_policy" {
  type    = string
  default = "https-only"
}

variable "origin_ssl_protocols" {
  type = list(string)
  default = ["TLSv1.2"]
}

variable "enabled" {
  type    = bool
  default = true
}

variable "allowed_methods" {
  type = list(string)
  default = ["DELETE", "GET", "HEAD", "OPTIONS", "PATCH", "POST", "PUT"]
}

variable "cached_methods" {
  type = list(string)
  default = ["GET", "HEAD"]
}

variable "compress" {
  type    = bool
  default = true
}

variable "viewer_protocol_policy" {
  type    = string
  default = "redirect-to-https"
}

variable "geo_restriction_type" {
  type    = string
  default = "none"
}

variable "use_default_certificate" {
  type    = bool
  default = true
}
