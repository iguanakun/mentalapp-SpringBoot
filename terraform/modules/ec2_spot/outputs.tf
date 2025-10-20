output "spot_instance_id" {
  description = "Spot instance ID"
  value       = aws_spot_instance_request.mentalapp.spot_instance_id
}

output "spot_request_id" {
  description = "Spot request ID"
  value       = aws_spot_instance_request.mentalapp.id
}

output "public_ip" {
  description = "Public IP address of the instance"
  value       = aws_spot_instance_request.mentalapp.public_ip
}

output "elastic_ip" {
  description = "Elastic IP address of the instance"
  value       = aws_eip.mentalapp.public_ip
}

output "elastic_ip_allocation_id" {
  description = "Elastic IP allocation ID"
  value       = aws_eip.mentalapp.id
}

output "instance_profile_arn" {
  description = "IAM instance profile ARN"
  value       = aws_iam_instance_profile.ec2_profile.arn
}

output "private_key_path" {
  description = "Path to the private key file"
  value       = local_file.private_key.filename
}

output "ssh_command" {
  description = "SSH command to connect to the instance (using Elastic IP)"
  value       = "ssh -i ${local_file.private_key.filename} ec2-user@${aws_eip.mentalapp.public_ip}"
}
