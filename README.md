# Image Recognition API

## Description
API for handling images and using **Amazon Reckognition**, for storage of none-GDPR images in **Amazon S3**. (WIP)

Image metadata will be stored in **Amazon DynamoDB**.

Docker images will be stored in Docker Hub, and project will have CI/CD integration with GitHub.

## Versioning
Version: 0.3.0
- Setup of working Image API for storing images in S3 and uploading metadata of Image to DynamoDB.
- Combined metadata and image service layers into one.
- Updated controller with new API endpoints for Images and Metadata.
- Added Image model, which contains pre-signed URL of Image in S3 and Metadata.

Next version: Unit tests and revision (if needed).

### Used Software (Updated after each new version)
- Java Eclipse Temurin 17
- Gradle
- Spring Boot
- Docker
- Docker Compose
- Github Workflows
- AWS DynamoDB
- AWS S3

<br>

**Developed by:** Linus Regander

**Latest update:** 2026-09-16