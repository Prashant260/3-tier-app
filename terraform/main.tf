module "vpc" {
  source = "./modules/vpc"
}

module "iam" {
  source = "./modules/iam"
}

module "ec2" {
  source               = "./modules/ec2"
  environment          = var.environment
  vpc_id               = module.vpc.vpc_id
  public_subnet_id     = module.vpc.public_subnet_id
  iam_instance_profile = module.iam.ec2_instance_profile_name
}

module "rds" {
  source                = "./modules/rds"
  environment           = var.environment
  vpc_id                = module.vpc.vpc_id
  private_subnet_ids    = module.vpc.private_subnet_ids
  db_name               = var.db_name
  db_username           = var.db_username
  db_password           = var.db_password
  app_security_group_id = module.ec2.app_security_group_id
}

module "cloudwatch" {
  source       = "./modules/cloudwatch"
  project_name = var.project_name
}

module "s3" {
  source      = "./modules/s3"
  bucket_name = var.app_logs_bucket_name
}

module "logs_export_lambda" {
  source              = "./modules/logs-export-lambda"
  project_name        = var.project_name
  cloudwatch_group    = module.cloudwatch.log_group_name
  destination_bucket  = module.s3.bucket_name
  destination_prefix  = "bloghub/backend"
  schedule_expression = "cron(0 9 * * ? *)"
}
