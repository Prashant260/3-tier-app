output "ec2_instance_profile_name" {
  value = aws_iam_instance_profile.ec2_profile.name
}

output "firehose_role_arn" {
  value = aws_iam_role.firehose.arn
}

output "cloudwatch_logs_role_arn" {
  value = aws_iam_role.cloudwatch_logs.arn
}
