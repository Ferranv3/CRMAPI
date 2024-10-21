# CRM Service

This is a CRM backend service built with Java 21 and Spring Boot 3. It follows the Hexagonal Architecture and SOLID principles. The service manages customers and users and is secured using OAuth 2.0.

## Features
- Manage customers (CRUD)
- Manage users (Admin role)
- OAuth 2.0 Authentication

## Requirements
- Java 21
- Docker
- Maven

## Running the Application
1. Build the project:
    ```bash
    mvn clean install
2. Run the application using Docker:
    ```bash
    docker-compose up