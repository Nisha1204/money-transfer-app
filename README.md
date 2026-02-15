# FintechCore: Enterprise Money Transfer System

## 1. Project Overview
[cite_start]FintechCore is a production-grade digital money transfer microservice developed as a training-aligned progressive build[cite: 6, 8, 11]. [cite_start]The system enables secure fund transfers between accounts while maintaining a complete transaction audit trail and advanced business intelligence analytics[cite: 16, 17].

## 2. System Architecture
[cite_start]The project follows a modern full-stack architecture integrated with a cloud data warehouse[cite: 23, 622]:
* [cite_start]**Frontend**: Angular 18+ (Standalone Components, Material UI)[cite: 78, 92].
* [cite_start]**Backend**: Spring Boot 3.x (Java 17 LTS, Spring Security, JPA)[cite: 77, 84, 88].
* [cite_start]**Database**: MySQL 8.x (OLTP for transactional data)[cite: 91, 470].
* [cite_start]**Analytics**: Snowflake Cloud (OLAP for business intelligence)[cite: 79, 93, 375].
* [cite_start]**Automation**: Python-based ETL pipeline for data synchronization[cite: 18, 384].



## 3. Key Features
* [cite_start]**Secure Fund Transfer**: ACID-compliant transactions with source/destination validation[cite: 280, 445, 528].
* [cite_start]**Admin Analytics**: Real-time BI dashboard powered by Snowflake, featuring success rates, peak hours, and transaction velocity[cite: 66, 407, 442].
* [cite_start]**Role-Based Access**: Dedicated workflows for USER and ADMIN roles[cite: 313, 325, 441, 551].
* [cite_start]**Data Integrity**: Idempotency checks to prevent duplicate transfers[cite: 248, 426].
* [cite_start]**Audit Trail**: Cross-cutting concerns handled via Spring AOP for method logging[cite: 260, 292, 448].

## 4. Database & Warehouse Design
### OLTP (MySQL)
[cite_start]Stores active accounts and live transaction logs[cite: 470]. 
* **Account IDs**: 10-digit enterprise format (starting at 1001001001).
* **Transaction IDs**: UUID/Reference number format (e.g., TRX-82910).

### OLAP (Snowflake)
[cite_start]Dimensional model designed for high-performance analytics[cite: 392, 396]:
* [cite_start]**Fact Table**: `FACT_TRANSACTIONS` (Grain: Individual Transaction)[cite: 383, 397].
* [cite_start]**Dimension Tables**: `DIM_ACCOUNT`, `DIM_DATE`[cite: 382, 392].

## 5. Setup & Installation
### Prerequisites
* [cite_start]Java 17 LTS[cite: 84].
* [cite_start]Node.js & Angular CLI[cite: 78].
* [cite_start]Python 3.11+ (for ETL)[cite: 79].
* [cite_start]MySQL 8.x[cite: 91].
* [cite_start]Snowflake Account[cite: 79, 101].

### Running the Project
1.  **Backend**: Start the Spring Boot application.
    * *Note*: Must use JVM flag: `--add-opens=java.base/java.nio=ALL-UNNAMED` for Snowflake JDBC compatibility.
2.  [cite_start]**Frontend**: Run `ng serve` from the `frontend/` directory[cite: 364].
3.  [cite_start]**ETL Pipeline**: Run `python snowflake_etl.py` to sync MySQL data to the Snowflake warehouse[cite: 623].

## 6. Automation
The ETL process is automated via **Windows Task Scheduler**, configured to run the Python sync script nightly at midnight to ensure the analytics warehouse remains up-to-date for business hours.

## [cite_start]7. Deliverables by Module [cite: 620]
* [cite_start]**Module 1**: GitHub repository with branching strategy[cite: 32, 136].
* [cite_start]**Module 2**: Java domain models and business logic with JUnit 5 tests[cite: 40, 76, 223].
* [cite_start]**Module 3**: RESTful APIs, Spring Security, and AOP logging[cite: 50, 77, 297].
* [cite_start]**Module 4**: Angular SPA with dashboard, transfers, and history views[cite: 59, 368].
* [cite_start]**Module 5**: Snowflake DW schema, ETL pipeline, and BI queries[cite: 66, 623].