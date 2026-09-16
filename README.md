# Image Recognition API

## Description
API for handling images and using **Amazon Reckognition**, for storage of none-GDPR images in **Amazon S3**. (WIP)

Image metadata will be stored in **Amazon DynamoDB**.

Docker images will be stored in Docker Hub, and project will have CI/CD integration with GitHub.

## Versioning
Version: 0.4.0
- Added global error handling
- Improved error handling in API
- Removed uneccessary try/catch calls.
- Fixed URL prefix in build of pre-signed URL.
- Added trace id on responses.

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