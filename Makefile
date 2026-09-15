SHELL := cmd.exe

ifeq ($(OS),Windows_NT)
    GRADLE := gradlew.bat
else
    GRADLE := ./gradlew
endif

PROJECT_NAME = $(shell $(GRADLE) -q properties --property name | grep "^name:" | cut -d' ' -f2)
PROJECT_VERSION = $(shell $(GRADLE) -q properties --property version | grep "^version:" | cut -d' ' -f2)
JAR_FILE = build/libs/$(PROJECT_NAME)-$(PROJECT_VERSION).jar

DOCKER_PORT := 8080

.PHONY: run build_jar run_jar test clean up down build logs ps

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

start:
	docker compose up --build

stop:
	docker compose down

build:
	docker compose build