provider "aws" {
  region = "us-east-1"
}

# S3 bucket for state
resource "aws_s3_bucket" "tf_state" {
  bucket = "prashant-terraform-state-bucket-12345" # must be globally unique

  tags = {
    Name = "terraform-state"
  }
}

# Enable versioning
resource "aws_s3_bucket_versioning" "versioning" {
  bucket = aws_s3_bucket.tf_state.id

  versioning_configuration {
    status = "Enabled"
  }
}

# DynamoDB table for locking
resource "aws_dynamodb_table" "tf_lock" {
  name         = "terraform-lock-table"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "LockID"

  attribute {
    name = "LockID"
    type = "S"
  }
}