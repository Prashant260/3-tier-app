output "log_group_name" {
  value = aws_cloudwatch_log_group.backend.name
}

output "firehose_name" {
  value = aws_kinesis_firehose_delivery_stream.cloudwatch_to_s3.name
}
