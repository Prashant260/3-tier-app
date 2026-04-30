# BlogHub 3-Tier App

BlogHub is a Spring Boot blogging application deployed with Docker on EC2, PostgreSQL on RDS, CloudWatch Logs for live application logs, and S3 for archived log exports.

## Project Structure

```text
.
├── app/                         # Spring Boot application
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── src/
├── terraform/                   # AWS infrastructure
│   ├── modules/
│   │   ├── cloudwatch/
│   │   ├── ec2/
│   │   ├── iam/
│   │   ├── logs-export-lambda/
│   │   ├── rds/
│   │   ├── s3/
│   │   └── vpc/
│   └── main.tf
└── .github/workflows/ci.yml      # CI/CD pipeline
```

## Runtime Environment

The application requires these environment variables:

```env
DATABASE_URL=jdbc:postgresql://bloghub-dev.c9a00siiy283.ap-south-1.rds.amazonaws.com:5432/bloghub
DATABASE_USERNAME=bloghub_admin
DATABASE_PASSWORD=your-db-password
```

The Spring Boot app listens on container port `8082`. Docker maps it to EC2 port `8080`.

## Docker Run

```bash
docker run -d \
  --name bloghub \
  -p 8080:8082 \
  -e DATABASE_URL='jdbc:postgresql://bloghub-dev.c9a00siiy283.ap-south-1.rds.amazonaws.com:5432/bloghub' \
  -e DATABASE_USERNAME='bloghub_admin' \
  -e DATABASE_PASSWORD='your-db-password' \
  prashant260/bloghub:latest
```

## Terraform Deploy

From the `terraform` directory:

```bash
terraform init
terraform apply -auto-approve -var="db_password=your-db-password"
```

Useful outputs:

```bash
terraform output app_server_ip
terraform output database_url
```

## CloudWatch Logs

The Docker container sends Spring Boot logs to CloudWatch using the Docker `awslogs` driver.

Log group:

```text
/bloghub/backend
```

View logs:

```bash
aws logs tail /bloghub/backend --region ap-south-1 --follow
```

## Export Logs to S3

CloudWatch keeps live logs. S3 stores archived exports.

S3 bucket:

```text
prashant-app-logs-storage-bucket-1-0777
```

Manual export command:

```bash
aws logs create-export-task \
  --region ap-south-1 \
  --log-group-name /bloghub/backend \
  --from $(date -d "1 day ago" +%s)000 \
  --to $(date +%s)000 \
  --destination prashant-app-logs-storage-bucket-1-0777 \
  --destination-prefix bloghub/backend
```

Check exported files:

```bash
aws s3 ls s3://prashant-app-logs-storage-bucket-1-0777/bloghub/backend/ --recursive --region ap-south-1
```

## CI/CD

The GitHub Actions workflow:

1. Applies Terraform.
2. Reads Terraform outputs.
3. Builds and pushes the Docker image.
4. Deploys the latest image to EC2.
5. Runs the container with database env vars and CloudWatch logging enabled.

Required GitHub secrets:

```text
AWS_ACCESS_KEY_ID
AWS_SECRET_ACCESS_KEY
DOCKERHUB_USERNAME
DOCKERHUB_TOKEN
EC2_SSH_PRIVATE_KEY
DB_PASSWORD
```

## Troubleshooting

If the container exits with:

```text
Driver org.postgresql.Driver claims to not accept jdbcUrl, ${DATABASE_URL}
```

then `DATABASE_URL` was not passed correctly to Docker. Use the exact variable name `DATABASE_URL`, not `DB_URL`, and do not add spaces around `=`.

Correct:

```bash
-e DATABASE_URL='jdbc:postgresql://host:5432/bloghub'
```

Incorrect:

```bash
-e DB_URL= 'jdbc:postgresql://host:5432/bloghub'
```
