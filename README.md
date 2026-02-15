# FintechCore: Enterprise Money Transfer System

## 1. Project Overview
FintechCore is a production-grade digital money transfer microservice developed as a training-aligned progressive build. The system enables secure fund transfers between accounts while maintaining a complete transaction audit trail and advanced business intelligence analytics.

## 2. System Architecture
The project follows a modern full-stack architecture integrated with a cloud data warehouse:
* **Frontend**: Angular 18+ (Standalone Components, Material UI)
* **Backend**: Spring Boot 3.x (Java 17 LTS, Spring Security, JPA)
* **Database**: MySQL 8.x (OLTP for transactional data)
* **Analytics**: Snowflake Cloud (OLAP for business intelligence)
* **Automation**: Python-based ETL pipeline for data synchronization



## 3. Key Features
* **Secure Fund Transfer**: ACID-compliant transactions with source/destination validation.
* **Admin Analytics**: Real-time BI dashboard powered by Snowflake, featuring success rates, peak hours, and transaction velocity.
* **Role-Based Access**: Dedicated workflows for USER and ADMIN roles.
* **Data Integrity**: Idempotency checks to prevent duplicate transfers.
* **Audit Trail**: Cross-cutting concerns handled via Spring AOP for method logging.

## 4. Database & Warehouse Design
### OLTP (MySQL)
Stores active accounts and live transaction logs. 
* **Account IDs**: 10-digit enterprise format (e.g., 1001001001).
* **Transaction IDs**: Standardized Reference number format (e.g., TRX-82910).

### OLAP (Snowflake)
Dimensional model designed for high-performance analytics:
* **Fact Table**: `FACT_TRANSACTIONS` (Grain: Individual Transaction).
* **Dimension Tables**: `DIM_ACCOUNTS`, `DIM_USERS`.

## 5. Setup & Installation
### Prerequisites
* Java 17 LTS
* Node.js & Angular CLI
* Python 3.11+ (with pandas, sqlalchemy, and snowflake-connector-python)
* MySQL 8.x
* Snowflake Account (ACCOUNTADMIN role)

### Running the Project
1.  **Backend**: Start the Spring Boot application using the JVM flag: `--add-opens=java.base/java.nio=ALL-UNNAMED`.
2.  **Frontend**: Run `ng serve` from the `frontend/` directory.
3.  **ETL Pipeline**: Run `python snowflake_etl.py` to sync MySQL data to the Snowflake warehouse.

## 6. Automation
The ETL process is automated via **Windows Task Scheduler**, configured to run the Python sync script nightly at midnight to ensure the analytics warehouse remains up-to-date for business hours.

## 7. Deliverables by Module
* **Module 1**: GitHub repository with branching strategy and clean commit history.
* **Module 2**: Java domain models, DTOs, and business logic with JUnit 5 tests.
* **Module 3**: RESTful APIs, Service layer, Security, and AOP logging.
* **Module 4**: Angular SPA featuring a Dashboard with balance cards, Transfer forms, and Transaction History.
* **Module 5**: Snowflake DW schema, Python ETL pipeline, and BI analytics queries.