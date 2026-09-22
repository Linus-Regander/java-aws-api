ifeq ($(OS),Windows_NT)
    GRADLE := gradlew.bat
else
    GRADLE := ./gradlew
endif

PROJECT_NAME = $(shell $(GRADLE) -q properties --property name | grep "^name:" | cut -d' ' -f2)
PROJECT_VERSION = $(shell $(GRADLE) -q properties --property version | grep "^version:" | cut -d' ' -f2)
JAR_FILE = build/libs/$(PROJECT_NAME)-$(PROJECT_VERSION).jar

DOCKER_PORT := 8080

.PHONY: run_gradle build_gradle build_jar run_jar test clean start_container stop_container build_container tf-aws tf-destroy-local

run_gradle: build_gradle
	$(GRADLE) bootRun

build_gradle:
	$(GRADLE) build

build_jar:
	$(GRADLE) build -x test
	java -jar $(JAR_FILE)

run_jar:
	java -jar $(JAR_FILE)

test:
	$(GRADLE) test

clean:
	$(GRADLE) clean

start_container:
	docker compose up --build

stop_container:
	docker compose down

build_container:
	docker compose build

tf-destroy-local:
	cd terraform && terraform destroy -auto-approve -var local_mode=true -var local_endpoint=http://localhost:4566

tf-aws:
	cd terraform && terraform apply -var local_mode=false