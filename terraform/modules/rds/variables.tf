variable "environment" {}

variable "vpc_id" {}

variable "private_subnet_ids" {
  type = list(string)
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

variable "app_security_group_id" {}
