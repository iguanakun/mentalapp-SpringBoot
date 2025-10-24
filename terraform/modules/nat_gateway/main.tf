# NATゲートウェイ用Elastic IP
resource "aws_eip" "this" {
  domain = "vpc"
  depends_on = [var.internet_gateway_id]

  tags = {
    Name = "${var.project_name}-nat-eip-${var.availability_zone_suffix}"
  }
}

# NATゲートウェイ
resource "aws_nat_gateway" "this" {
  allocation_id = aws_eip.this.id
  subnet_id     = var.public_subnet_id

  tags = {
    Name = "${var.project_name}-nat-gw-${var.availability_zone_suffix}"
  }

  depends_on = [var.internet_gateway_id]
}

# プライベートルートテーブル
resource "aws_route_table" "private" {
  vpc_id = var.vpc_id

  route {
    cidr_block     = "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.this.id
  }

  tags = {
    Name = "${var.project_name}-private-rt-${var.availability_zone_suffix}"
  }
}

# ルートテーブルアソシエーション
resource "aws_route_table_association" "private" {
  subnet_id      = var.private_subnet_id
  route_table_id = aws_route_table.private.id
}
