variable "region" {
  type    = string
  default = "eu-north-1"
}

variable "local_mode" {
  description = "toggles local mode (LocalStack) vs. AWS"
  type        = bool
  default     = true
}

variable "local_endpoint" {
  type    = string
  default = "http://localhost:4566"
}

variable "dynamodb_table_name" {
  type    = string
  default = "image-metadata"
}

variable "s3_bucket_name" {
  description = "Globally unique S3 bucket name"
  type        = string
  default     = "image-recognitioner"
}