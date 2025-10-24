output "spot_instance_id" {
  value       = aws_spot_instance_request.mentalapp.spot_instance_id
}

output "spot_request_id" {
  value       = aws_spot_instance_request.mentalapp.id
}

output "public_ip" {
  value       = aws_spot_instance_request.mentalapp.public_ip
}

output "elastic_ip" {
  value       = aws_eip.mentalapp.public_ip
}

output "elastic_ip_allocation_id" {
  value       = aws_eip.mentalapp.id
}

output "instance_profile_arn" {
  value       = aws_iam_instance_profile.ec2_profile.arn
}

output "private_key_path" {
  value       = local_file.private_key.filename
}

output "ssh_command" {
  value       = "ssh -i ${local_file.private_key.filename} ec2-user@${aws_eip.mentalapp.public_ip}"
}
