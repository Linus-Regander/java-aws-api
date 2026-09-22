output "dynamodb_table_name" {
  value = aws_dynamodb_table.images.name
}

output "s3_bucket_name" {
  value = aws_s3_bucket.images.bucket
}

output "region" {
  value = var.region
}