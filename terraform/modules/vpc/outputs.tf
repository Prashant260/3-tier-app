output "vpc_id" {
  value = aws_vpc.new.id
}

output "public_subnet_id" {
  value = aws_subnet.pub_subnet.id
}

output "private_subnet_id" {
  value = aws_subnet.pri_subnet.id
}

output "private_subnet_ids" {
  value = [
    aws_subnet.pri_subnet.id,
    aws_subnet.pri_subnet_2.id
  ]
}

output "internet_gateway_id" {
  value = aws_internet_gateway.igw.id
}
output "route_table_id" {
  value = aws_route_table.public_rt.id
}
