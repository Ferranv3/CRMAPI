# CRM Service
![Build Status](https://github.com/Ferranv3/CRMAPI/actions/workflows/ci.yml/badge.svg?branch=main)

This is a CRM backend service built with Java 21 and Spring Boot 3. It follows Hexagonal Architecture, DDD and SOLID principles to ensure a modular, testable, and maintainable codebase. The service manages customers and users, with a secure JWT-based authentication system.

## Features
- Customer Management (CRUD): Endpoints to create, read, update, and delete customer records.
- User Management: Admin-specific operations for managing users and their roles.
- Role-based Access Control: Restricted endpoints based on user roles, such as Admin.
- JWT Authentication: Secure authentication and authorization using JSON Web Tokens (JWT).

## Testing
The project includes comprehensive testing to ensure functionality and security:

- Unit Tests: Focus on individual service and component logic, including validation of domain models.
- Integration Tests: Validate correct functioning of controllers and service interactions with the database, including:
  - Customer CRUD operations
  - User management and role updates
  - JWT-based authentication and protected endpoints

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

## Running tests
1. Run all tests of the project:
    ```bash
    mvn test