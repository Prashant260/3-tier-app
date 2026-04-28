output "app_server_ip" {
  value = aws_instance.app_server.public_ip
}

output "app_security_group_id" {
  value = aws_security_group.app_sg.id
}
