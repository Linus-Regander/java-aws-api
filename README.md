<div align="center">

# Image Recognition API

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F?logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Build](https://github.com/Linus-Regander/java-aws-api/actions/workflows/ci.yml/badge.svg)

</div>

## Description
API for handling images and using **Amazon Rekognition**, for storage of non-GDPR images in **Amazon S3**. (*This is currently done in a local setting*).

Image metadata is stored in **Amazon DynamoDB**.

Docker images are stored in Docker Hub, with CI/CD integration via GitHub Actions.

## Versioning

**Current version:** 0.5.0
- Added tags and updated README.md.
- Added unit tests for controller, repository and service classes.
- Added tests to CI/CD workflow.
- Added code coverage to Github Actions.

**Next version:** Terraform integration for live AWS integration.

## Tech Stack
| Category | Tools |
|---|---|
| Language | Java 17 (Eclipse Temurin) |
| Framework | Spring Boot |
| Build | Gradle |
| Containers | Docker, Docker Compose |
| CI/CD | GitHub Actions |
| Cloud | AWS DynamoDB, AWS S3 |

---

<div align="center">

**Developed by** Linus Regander
**Latest update:** 2026-09-16

</div>