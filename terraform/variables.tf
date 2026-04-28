variable "aws_region" {
  default = "ap-south-1"
}

variable "environment" {
  default = "dev"
}

variable "project_name" {
  default = "bloghub"
}

variable "app_logs_bucket_name" {
  default = "prashant-app-logs-storage-bucket-1-0777"
}

variable "db_name" {
  default = "bloghub"
}

variable "db_username" {
  default = "bloghub_admin"
}

variable "db_password" {
  sensitive = true
}
