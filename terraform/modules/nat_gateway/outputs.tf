output "nat_gateway_id" {
  description = "NAT Gateway ID"
  value       = aws_nat_gateway.this.id
}

output "nat_gateway_public_ip" {
  description = "NAT Gateway Public IP"
  value       = aws_eip.this.public_ip
}

output "elastic_ip_id" {
  description = "Elastic IP ID"
  value       = aws_eip.this.id
}

output "private_route_table_id" {
  description = "Private Route Table ID"
  value       = aws_route_table.private.id
}
