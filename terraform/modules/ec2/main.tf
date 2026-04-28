variable "environment" {}

variable "vpc_id" {}

variable "public_subnet_id" {}

variable "iam_instance_profile" {}

# -----------------------------
# SECURITY GROUPS
# -----------------------------
resource "aws_security_group" "app_sg" {
  name   = "app-sg-${var.environment}"
  vpc_id = var.vpc_id

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "backend_sg" {
  name   = "backend-sg-${var.environment}"
  vpc_id = var.vpc_id

  ingress {
    from_port       = 5000
    to_port         = 5000
    protocol        = "tcp"
    security_groups = [aws_security_group.app_sg.id]
  }

  ingress {
    from_port       = 22
    to_port         = 22
    protocol        = "tcp"
    security_groups = [aws_security_group.app_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# -----------------------------
# FRONTEND EC2
# -----------------------------
resource "aws_instance" "app_server" {
  ami                    = "ami-05d2d839d4f73aafb"
  instance_type          = "t3.micro"
  subnet_id              = var.public_subnet_id
  vpc_security_group_ids = [aws_security_group.app_sg.id]
  iam_instance_profile   = var.iam_instance_profile

  key_name = "flask"

  user_data = <<-EOF
              #!/bin/bash
              apt update -y
              apt install docker.io docker-compose-v2 -y
              systemctl start docker
              systemctl enable docker
              usermod -aG docker ubuntu
              chmod 666 /var/run/docker.sock
              EOF

  tags = {
    Name = "frontend-${var.environment}"
  }
}
