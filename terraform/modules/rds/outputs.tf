output "database_endpoint" {
  value = aws_db_instance.bloghub.endpoint
}

output "database_url" {
  value = "jdbc:postgresql://${aws_db_instance.bloghub.endpoint}/${aws_db_instance.bloghub.db_name}"
}

output "database_name" {
  value = aws_db_instance.bloghub.db_name
}
