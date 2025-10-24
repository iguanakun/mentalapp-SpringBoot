output "vpc_id" {
  value       = aws_vpc.this.id
}

output "public_subnet_id" {
  value       = aws_subnet.public_1a.id
}

# 将来の拡張用にコメントアウト
# output "private_subnet_id" {
#   description = "Private subnet ID"
#   value       = aws_subnet.private_1a.id
# }

output "internet_gateway_id" {
  value       = aws_internet_gateway.this.id
}