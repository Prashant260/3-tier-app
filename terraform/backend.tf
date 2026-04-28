terraform {
  backend "s3" {
    bucket         = "prashant-terraform-state-bucket-12345"
    key            = "app/terraform.tfstate"
    region         = "us-east-1"
    dynamodb_table = "terraform-lock-table"
    encrypt        = true
  }
}
