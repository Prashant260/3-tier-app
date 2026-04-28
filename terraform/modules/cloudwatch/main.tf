variable "project_name" {}

variable "log_retention_days" {
  default = 30
}

resource "aws_cloudwatch_log_group" "backend" {
  name              = "/${var.project_name}/backend"
  retention_in_days = var.log_retention_days

  tags = {
    Name = "${var.project_name}-backend-logs"
  }
}
