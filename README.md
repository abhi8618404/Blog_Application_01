# Employee Management Platform

![Java](https://img.shields.io/badge/Java_17-007396?logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?logo=springboot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=white)
![Jenkins](https://img.shields.io/badge/Jenkins-D24939?logo=jenkins&logoColor=white)
![Render](https://img.shields.io/badge/Render_Cloud-2A2A72?logo=cloud&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)

A full-stack employee directory that pairs a Spring Boot REST API with a React
single-page application. Managers can create, update, browse, and remove
employee records while the backend persists data in MySQL and exposes a clean
JSON API.

---

## Features

- **Employee CRUD** – create, edit, view, and delete employee profiles.
- **Responsive UI** – React + Bootstrap tables and forms optimized for desktop.
- **REST API** – `/api/v1/employees` endpoints secured with validation and
  helpful error responses.
- **Repository abstraction** – Spring Data JPA repositories with transactional
  semantics.
- **Tests** – unit tests for repositories/controllers plus H2-backed
  integration tests.
- **Configurable persistence** – MySQL for production, in-memory H2 for tests.

---

## Live Demo

Visit the production deployment hosted on Render:

[`https://blog-application-01.onrender.com/`](https://blog-application-01.onrender.com/)

The service runs the containerized Spring Boot API behind a Render web service,
fronted by the React build artifacts.

---

## Screenshot

![Employee Management UI](https://github.com/user-attachments/assets/738e4648-b516-49d2-8d05-78bb4484c6c6)

---

## Deployment Overview

- **Containerization** – the backend is packaged as a Docker image and pushed to
  the Render registry. Environment variables supply DB credentials and app
  secrets at runtime.
- **CI/CD with Jenkins** – a Jenkins pipeline handles Maven tests, Docker builds,
  and pushes on every `main` branch change. Successful builds trigger a Render
  deploy hook to roll out the new container.
- **Render hosting** – Render serves both the static React build (static site
  service) and the Spring Boot container (web service) on scalable infrastructure
  with HTTPS termination.

---

## Project Layout

```
Employee_Management/
├── Backend/                # Spring Boot application
│   ├── src/main/java/...   # Controllers, models, repositories
│   ├── src/test/java/...   # Unit and integration tests
│   └── pom.xml             # Maven configuration
├── src/                    # React SPA (Create React App)
├── public/                 # Static assets for React
├── package.json            # Frontend dependencies & scripts
└── README.md               # You are here
```

---

## Tech Stack

| Layer     | Technology                                      |
|-----------|--------------------------------------------------|
| Backend   | Spring Boot 3, Spring Web, Spring Data JPA       |
| Database  | MySQL 8 (prod), H2 (tests)                       |
| Frontend  | React 16, React Router, Axios, Bootstrap 4       |
| Tooling   | Maven 3.9+, npm, Jest/Testing Library            |

---

## Getting Started

### Prerequisites

- Java 17 JDK
- Maven 3.9+
- Node.js 16+ and npm
- MySQL server running locally

### 1. Backend setup

```bash
cd Backend
mvn clean install
mvn spring-boot:run
```

The API is served at `http://localhost:8080/api/v1/employees`.

#### Database configuration

Edit `Backend/src/main/resources/application.properties` to match your MySQL
credentials:

```
spring.datasource.url=jdbc:mysql://localhost:3306/<db_name>?allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=<user>
spring.datasource.password=<password>
spring.jpa.hibernate.ddl-auto=update
```

### 2. Frontend setup

```bash
npm install
npm start
```

The React development server runs on `http://localhost:3000` and proxies API
requests to the backend (CORS is enabled for this origin).

---

## Running Tests

| Target   | Command                | Notes                                |
|----------|------------------------|--------------------------------------|
| Backend  | `cd Backend && mvn test` | Loads an H2 in-memory database.      |
| Frontend | `npm test`             | Jest + Testing Library watch mode.   |

---

## REST API

| Method | Endpoint                | Description             |
|--------|-------------------------|-------------------------|
| GET    | `/api/v1/employees`     | List all employees      |
| GET    | `/api/v1/employees/{id}`| Fetch a single employee |
| POST   | `/api/v1/employees`     | Create a new employee   |
| PUT    | `/api/v1/employees/{id}`| Update an employee      |
| DELETE | `/api/v1/employees/{id}`| Delete an employee      |

`POST` and `PUT` accept JSON payloads shaped like:

```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "emailId": "jane.doe@example.com"
}
```




## Status

This project is feature-complete and currently deployed on Render. No further
enhancements are planned at the moment.

Happy hacking! 🎯
