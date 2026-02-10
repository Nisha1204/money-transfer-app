# Money Transfer System

A **digital money transfer application** built progressively as a full-stack project.  
The system demonstrates **enterprise backend design**, **secure REST APIs**, **modern frontend development**, and **analytics using Snowflake**.

---

## Project Overview

Modern banking systems require secure, reliable, and auditable fund transfer mechanisms.  
This project implements a **microservice-based money transfer system** with:

- Secure account-to-account transfers
- Complete transaction audit trail
- RESTful backend services
- Angular-based frontend UI
- Analytical reporting using Snowflake

The project is designed to be built **module-by-module**, aligning with structured training phases.

---

## Objectives

- Build a **real-world banking microservice**
- Apply concepts from:
    - Git & branching strategies
    - Advanced Java (Java 17)
    - Spring Boot & Spring Security
    - Angular (SPA)
    - Snowflake analytics
- Emphasize **clean architecture**, **scalability**, and **best practices**

---

## Technology Stack

### Backend
- Java 17
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- Spring AOP
- MySQL 8.x
- Maven

### Frontend
- Angular 15+
- TypeScript
- Angular Material
- RxJS

### Analytics
- Snowflake Cloud
- SQL
- ETL using `COPY INTO`

### Tooling
- Git & GitHub
- JUnit 5
- Lombok
- Postman

---

## 📂 Project Directory Structure

```text
money-transfer-system/
│
├── backend/                      # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/moneytransfer/
│   │   │   │       ├── controller/        # REST controllers
│   │   │   │       ├── service/           # Business logic
│   │   │   │       ├── repository/        # JPA repositories
│   │   │   │       ├── entity/            # Entities
│   │   │   │       ├── enum/              # Enums
│   │   │   │       ├── dto/               # Request/response DTOs
│   │   │   │       ├── exception/         # Custom exceptions
│   │   │   │       ├── config/            # Security & app config
│   │   │   │       └── aspect/            # AOP logging
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── data.sql
│   │   └── test/                          # Unit & integration tests
│   └── pom.xml
│
├── frontend/                     # Angular frontend
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── login/
│   │   │   │   ├── dashboard/
│   │   │   │   ├── transfer/
│   │   │   │   └── history/
│   │   │   ├── services/
│   │   │   ├── guards/
│   │   │   └── interceptors/
│   │   └── environments/
│   └── angular.json
│
├── database/                     # OLTP database scripts
│   ├── schema.sql
│   └── seed-data.sql
│
├── snowflake/                    # Data warehouse & analytics
│   ├── ddl/
│   ├── etl/
│   └── analytics-queries.sql
│
├── docs/                         # Architecture & design docs
│
├── .gitignore
└── README.md
