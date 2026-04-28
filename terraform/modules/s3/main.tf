variable "bucket_name" {}

resource "aws_s3_bucket" "app_logs" {
  bucket = var.bucket_name

  tags = {
    Name = "log-storage"
  }
}

resource "aws_s3_bucket_versioning" "app_logs" {
  bucket = aws_s3_bucket.app_logs.id

  versioning_configuration {
    status = "Enabled"
  }
}
