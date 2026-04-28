variable "project_name" {}

variable "s3_bucket_arn" {}

variable "firehose_role_arn" {}

variable "cloudwatch_logs_role_arn" {}

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

resource "aws_kinesis_firehose_delivery_stream" "cloudwatch_to_s3" {
  name        = "${var.project_name}-cloudwatch-to-s3"
  destination = "extended_s3"

  extended_s3_configuration {
    role_arn            = var.firehose_role_arn
    bucket_arn          = var.s3_bucket_arn
    buffering_interval  = 300
    buffering_size      = 5
    compression_format  = "GZIP"
    prefix              = "cloudwatch/${var.project_name}/year=!{timestamp:yyyy}/month=!{timestamp:MM}/day=!{timestamp:dd}/"
    error_output_prefix = "cloudwatch-errors/${var.project_name}/year=!{timestamp:yyyy}/month=!{timestamp:MM}/day=!{timestamp:dd}/!{firehose:error-output-type}/"
  }
}

resource "aws_cloudwatch_log_subscription_filter" "s3_export" {
  name            = "${var.project_name}-logs-to-s3"
  log_group_name  = aws_cloudwatch_log_group.backend.name
  filter_pattern  = ""
  destination_arn = aws_kinesis_firehose_delivery_stream.cloudwatch_to_s3.arn
  role_arn        = var.cloudwatch_logs_role_arn
}

resource "aws_cloudwatch_log_group" "firehose_errors" {
  name              = "/aws/kinesisfirehose/${var.project_name}-cloudwatch-to-s3"
  retention_in_days = 30

  tags = {
    Name = "${var.project_name}-firehose-errors"
  }
}
