output "app_server_ip" {
  value = module.ec2.app_server_ip
}

output "app_instance_id" {
  value = module.ec2.app_instance_id
}

output "database_endpoint" {
  value = module.rds.database_endpoint
}

output "database_url" {
  value = module.rds.database_url
}

output "cloudwatch_log_group_name" {
  value = module.cloudwatch.log_group_name
}

output "logs_bucket_name" {
  value = module.s3.bucket_name
}
