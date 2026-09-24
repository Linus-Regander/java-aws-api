<div align="center">

# Image Recognition API

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Build](https://github.com/Linus-Regander/java-aws-api/actions/workflows/ci.yml/badge.svg)
[![Coverage](https://codecov.io/gh/Linus-Regander/java-aws-api/branch/main/graph/badge.svg)](https://codecov.io/gh/Linus-Regander/java-aws-api)

</div>

## Description
API for handling images and using **Amazon Rekognition** for moderating images and preventing upload of inappropriate content, for storage of non-GDPR images in **Amazon S3**. (*This is currently done in a local setting*).

Image metadata is stored in **Amazon DynamoDB**.

Docker images are stored in Docker Hub, with CI/CD integration via GitHub Actions.

## Versioning

**Current version:** 0.7.0
- Added Amazon Rekognition integration with client.
- Added image moderation for tagging and preventing upload of inappropriate images.
- Updated Terraform and configuration for Rekognition support.

**Next version:** Authentication and authorization in API.

## Tech Stack
| Category | Tools |
|---|---|
| Language | **Java 17** (Eclipse Temurin) |
| Framework | **Spring Boot** |
| Build | Gradle, **Terraform** |
| Containers | **Docker**, Docker Compose |
| CI/CD (GitHub) | **Actions**, CoPilot Agent, Workflows |
| Cloud (AWS) | **DynamoDB**, **S3**, **Rekognition**, Localstack |

---

<div align="center">

**Developed by** Linus Regander
**Latest update:** 2026-09-21

</div>