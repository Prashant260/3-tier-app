resource "aws_security_group" "rds_sg" {
  name   = "rds-sg-${var.environment}"
  vpc_id = var.vpc_id

  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [var.app_security_group_id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_subnet_group" "bloghub" {
  name       = "bloghub-db-subnet-group-${var.environment}"
  subnet_ids = var.private_subnet_ids

  tags = {
    Name = "bloghub-db-subnet-group-${var.environment}"
  }
}

resource "aws_db_instance" "bloghub" {
  identifier             = "bloghub-${var.environment}"
  allocated_storage      = 20
  engine                 = "postgres"
  instance_class         = "db.t3.micro"
  db_name                = var.db_name
  username               = var.db_username
  password               = var.db_password
  db_subnet_group_name   = aws_db_subnet_group.bloghub.name
  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  publicly_accessible    = false
  skip_final_snapshot    = true

  tags = {
    Name = "bloghub-rds-${var.environment}"
  }
}
