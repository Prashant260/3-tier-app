resource "aws_vpc" "new" {
  cidr_block = "10.0.0.0/16"

  tags = {
    Name = "new-vpc"
  }
}
resource "aws_subnet" "pub_subnet" {
  vpc_id                  = aws_vpc.new.id
  cidr_block              = "10.0.1.0/24"
  availability_zone       = "ap-south-1a"
  map_public_ip_on_launch = true
}

resource "aws_subnet" "pri_subnet" {
  vpc_id                  = aws_vpc.new.id
  cidr_block              = "10.0.2.0/24"
  availability_zone       = "ap-south-1a"
  map_public_ip_on_launch = false
}

resource "aws_subnet" "pri_subnet_2" {
  vpc_id                  = aws_vpc.new.id
  cidr_block              = "10.0.3.0/24"
  availability_zone       = "ap-south-1b"
  map_public_ip_on_launch = false
}

resource "aws_internet_gateway" "igw" {
  vpc_id = aws_vpc.new.id

  tags = {
    Name = "new-igw"
  }

}

resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.new.id

  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }

  tags = {
    Name = "pub-route-table"
  }
}

resource "aws_route_table_association" "pub_subnet_association" {
  subnet_id      = aws_subnet.pub_subnet.id
  route_table_id = aws_route_table.public_rt.id
}


resource "aws_eip" "nat_eip" {
  domain = "vpc"
}


resource "aws_nat_gateway" "nat_gw" {
  allocation_id = aws_eip.nat_eip.id
  subnet_id     = aws_subnet.pub_subnet.id

  tags = {
    Name = "nat-gw"
  }
}

resource "aws_route_table" "private_rt" {
  vpc_id = aws_vpc.new.id

  route {
    nat_gateway_id = aws_nat_gateway.nat_gw.id
    cidr_block     = "0.0.0.0/0"
  }
}

resource "aws_route_table_association" "pri_subnet_association" {
  subnet_id      = aws_subnet.pri_subnet.id
  route_table_id = aws_route_table.private_rt.id
}

resource "aws_route_table_association" "pri_subnet_2_association" {
  subnet_id      = aws_subnet.pri_subnet_2.id
  route_table_id = aws_route_table.private_rt.id
}
