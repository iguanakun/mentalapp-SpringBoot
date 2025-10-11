output "vpc_id" {
  description = "VPC ID"
  value       = aws_vpc.this.id
}

output "public_subnet_id" {
  description = "Public subnet ID"
  value       = aws_subnet.public_1a.id
}

# 将来の拡張用にコメントアウト
# output "private_subnet_id" {
#   description = "Private subnet ID"
#   value       = aws_subnet.private_1a.id
# }

output "internet_gateway_id" {
  description = "Internet Gateway ID"
  value       = aws_internet_gateway.this.id
}