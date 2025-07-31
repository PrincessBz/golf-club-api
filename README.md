# Golf Club Management System


- **Golf Club Management System**
    - REST API for managing golf club members and tournaments
    - Built with Spring Boot and Docker for easy deployment

- **Features**
    - Full CRUD for Members and Tournaments
    - Add members to tournaments (relational mapping)
    - Advanced search for members and tournaments
    - DTO-centric architecture for security and flexibility
    - Docker & Docker Compose for setup
    - Interactive API docs (Swagger UI)
    - Automated CI/CD with GitHub Actions

- **Technologies Used**
    - Java 21, Spring Boot
    - MySQL, Spring Data JPA
    - Docker, Docker Compose
    - JUnit 5, Mockito, Testcontainers
    - Maven
    - SpringDoc (Swagger UI)

- **Prerequisites**
    - Java 21
    - Maven
    - Docker & Docker Compose

- **How to Run Locally (with Docker)**
    - Clone the repo:  
      `git clone https://github.com/PrincessBz/golf-club-api.git`
    - Enter the directory:  
      `cd golf-club-api`
    - Build the app:  
      `mvn clean install`
    - Start services:  
      `docker-compose up --build`
    - API available at:  
      `http://localhost:8080`

- **Cloud Deployment (with AWS RDS)**
    - Create AWS RDS MySQL instance (public access: Yes)
    - Update `docker-compose.aws.yml` with RDS credentials
    - Start app:  
      `docker-compose -f docker-compose.aws.yml up --build`
    - API uses your AWS database

- **API Endpoints**
    - Swagger UI: `http://localhost:8080/swagger-ui.html`
    - Members: `/api/members`
    - Tournaments: `/api/tournaments`
    - (Full details in Swagger UI)

- **Testing**
    - Unit tests: `mvn test`
    - Integration tests: `mvn verify`
    - Testcontainers for MySQL integration tests
    - JUnit 5 and Mockito for unit tests

- **Author**
  Princess Bazunu


