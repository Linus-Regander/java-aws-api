ifeq ($(OS),Windows_NT)
    GRADLE := gradlew.bat
else
    GRADLE := ./gradlew
endif

PROJECT_NAME := $(shell $(GRADLE) -q properties --property name | grep "^name:" | cut -d' ' -f2)
PROJECT_VERSION := $(shell $(GRADLE) -q properties --property version | grep "^version:" | cut -d' ' -f2)
JAR_FILE := build/libs/$(PROJECT_NAME)-$(PROJECT_VERSION).jar

DOCKER_IMAGE := image-recognitioner
DOCKER_PORT := 8080

.PHONY: run build_jar run_jar test clean docker_build docker_run docker

run:
	$(GRADLE) bootRun

build_jar:
	$(GRADLE) build -x test
	java -jar $(JAR_FILE)

run_jar:
	java -jar $(JAR_FILE)

test:
	$(GRADLE) test

clean:
	$(GRADLE) clean

docker_build:
	docker build -t $(DOCKER_IMAGE) .

docker_run:
	docker run -p $(DOCKER_PORT):$(DOCKER_PORT) $(DOCKER_IMAGE)

docker: docker_build docker_run