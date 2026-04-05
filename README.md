# Financial Dashboard — Backend

A RESTful API for managing financial records with role-based access control and dashboard analytics, built as part of the Zorvyn technical assignment for Backend Engineer Intern.

---
## Contents

1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Architecture](#architecture)
4. [Request Flow](#request-flow)
5. [Data Model](#data-model)
6. [Authentication](#authentication)
7. [Role-Based Access Control](#role-based-access-control)
8. [API Reference](#api-reference)
9. [Validation and Error Handling](#validation-and-error-handling)
10. [Design Decisions and Assumptions](#design-decisions-and-assumptions)
11. [Tradeoffs and Simplifications](#tradeoffs-and-simplifications)
12. [Getting Started](#getting-started)
13. [Project Structure](#project-structure)
14. [Contact Details](#contact-details)

---
## Features

- JWT-based stateless authentication with configurable token expiry
- Hierarchical role-based access control (ADMIN > ANALYST > VIEWER) enforced at the endpoint level
- Full CRUD on financial records with soft delete to preserve historical data
- Dynamic filtering on records and users via JPA Specifications (date range, type, category, amount, role, active status)
- Pagination and sorting on all list endpoints
- Dashboard analytics — total income, total expenses, net balance, category-wise breakdowns, recent activity, and weekly/monthly trend data with zero-gap filling
- Bean Validation on all request DTOs with a custom cross-field constraint enforcing category-type consistency
- Centralized global exception handler returning consistent JSON error responses
- Categories defined as enums and validated against record type at both DTO and service level
- MapStruct-based DTO mapping with no manual field copying

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot |
| Security | Spring Security + JWT |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8.0 |
| Mapping | MapStruct + Lombok |

---

## Architecture

Layered architecture with clean separation of concerns:

```
┌──────────────────────────────────────────────┐
│                  Controllers                  │
│   Auth · Admin · FinancialRecord · Dashboard │
├──────────────────────────────────────────────┤
│                   Services                    │
│  Auth · User · FinancialRecord · Dashboard   │
├──────────────────────────────────────────────┤
│              Repositories (JPA)               │
│      UserRepo · RoleRepo · RecordRepo        │
├──────────────────────────────────────────────┤
│                MySQL Database                 │
└──────────────────────────────────────────────┘
```

Cross-cutting concerns:
- **Security** — `AuthTokenFilter` → `JWTService` → `CustomUserDetailsService`
- **Exception Handling** — Centralized `@RestControllerAdvice` global handler
- **DTO Mapping** — MapStruct mappers (`UserMapper`, `FinancialRecordMapper`)
- **Dynamic Queries** — JPA Specifications (`UserSpecification`, `FinancialRecordSpecification`)

---

## Request Flow

Every incoming request passes through the following chain before a response is returned:

```
HTTP Request
     │
     ▼
AuthTokenFilter
  (extract & validate JWT)
     │
     ▼
Spring Security Context
  (set authenticated principal)
     │
     ▼
Controller
  (parse & validate request DTO via Bean Validation)
     │
     ▼
Service Layer
  (business logic, authorization checks, cross-field validation)
     │
     ▼
Repository / JPA Specification
  (database query)
     │
     ▼
MapStruct Mapper
  (entity → response DTO)
     │
     ▼
HTTP Response

     ╳  (on any exception)
     ▼
GlobalExceptionHandler
  (consistent JSON error response)
```

---

## Data Model

Three core entities:

| Entity | Key Fields |
|---|---|
| **User** | `id`, `name`, `email`, `password` (hashed), `active`, `createdAt`, `role` (FK) |
| **Role** | `id`, `name` — one of `ADMIN`, `ANALYST`, `VIEWER` |
| **FinancialRecord** | `id`, `amount`, `type`, `category`, `recordDate`, `description`, `createdAt`, `deleted` (soft delete flag) |

Relationships: `User` → `Role` (Many-to-One). `FinancialRecord` is an independent entity with no foreign key to `User`.

---

## Authentication

JWT-based stateless authentication — no server-side session state is maintained.

**Login flow:**
1. `POST /api/auth/login` with email and password
2. Receive a JWT access token
3. Pass the token in all subsequent requests:

```
Authorization: Bearer <your_jwt_token>
```

Token expiry is configurable via `security.jwt.expiration-time` (default: 6 minutes). Expired or invalid tokens return `401 Unauthorized`.

---

## Role-Based Access Control

Three roles with hierarchical permissions — each higher role inherits all permissions of the roles below it:

```
ADMIN  ──▶  ANALYST  ──▶  VIEWER
```

| Role | Capabilities |
|---|---|
| **ADMIN** | Full access — manage users, create/update/delete financial records, view all analytics |
| **ANALYST** | Read financial records with filtering, pagination, and sorting; view dashboard and analytics |
| **VIEWER** | View dashboard summaries, trends, category breakdowns, and recent activity |

Access control is enforced at the endpoint level using Spring Security method-level annotations.

---

## API Reference

### Auth

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/login` | Public | Authenticate and receive a JWT token |

**Request body:**

| Field | Type | Required | Notes |
|---|---|---|---|
| `email` | String | Yes | Must be a valid email |
| `password` | String | Yes | — |

```json
{
  "email": "admin@example.com",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "name": "Admin User",
    "email": "admin@example.com",
    "role": "ADMIN",
    "active": true,
    "createdAt": "2025-01-15T10:30:00"
  }
}
```

---

### User Management

> Requires **ADMIN** role.

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/admin/users` | Create a new user |
| `GET` | `/api/admin/users` | List users with filtering, pagination, and sorting |
| `GET` | `/api/admin/users/{userId}` | Get a user by ID |
| `PUT` | `/api/admin/users/{userId}` | Update user details, role, password, or active status |
| `DELETE` | `/api/admin/users/{userId}` | Delete a user |

---

**POST `/api/admin/users` — Create user**

Request body:

| Field | Type | Required | Notes |
|---|---|---|---|
| `name` | String | Yes | Non-blank |
| `email` | String | Yes | Valid format, must be unique |
| `password` | String | Yes | Min 8 characters |
| `role` | String | No | Defaults to `VIEWER`. Accepted: `ADMIN`, `ANALYST`, `VIEWER` |

```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "securePass123",
  "role": "ANALYST"
}
```

Response (201):
```json
{
  "response": "User Created Successfully",
  "user": {
    "id": 5,
    "name": "Jane Doe",
    "email": "jane@example.com",
    "role": "ANALYST",
    "active": true,
    "createdAt": "2025-03-20T14:22:00"
  }
}
```

---

**GET `/api/admin/users` — List users**

Query parameters:

| Parameter | Type | Required | Default | Description |
|---|---|---|---|---|
| `name` | String | No | — | Partial match on name |
| `email` | String | No | — | Partial match on email |
| `role` | String | No | — | Filter by role (`ADMIN`, `ANALYST`, `VIEWER`) |
| `active` | Boolean | No | — | Filter by active status |
| `page` | Integer | No | `0` | Page number (zero-indexed) |
| `size` | Integer | No | `10` | Page size |
| `sortBy` | String | No | `id` | Sort field |
| `ascending` | Boolean | No | `true` | Sort direction |

---

**GET `/api/admin/users/{userId}` — Get user by ID**

Response (200):
```json
{
  "id": 5,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "role": "ANALYST",
  "active": true,
  "createdAt": "2025-03-20T14:22:00"
}
```

---

**PUT `/api/admin/users/{userId}` — Update user**

All fields are optional. Only provided fields are updated.

| Field | Type | Required | Notes |
|---|---|---|---|
| `name` | String | No | Non-blank if provided |
| `email` | String | No | Valid format, must be unique if provided |
| `password` | String | No | Min 8 characters if provided |
| `role` | String | No | `ADMIN`, `ANALYST`, or `VIEWER` |
| `active` | Boolean | No | — |

```json
{
  "name": "Jane Smith",
  "email": "janesmith@example.com",
  "password": "newSecurePass456",
  "role": "ADMIN",
  "active": false
}
```

Response (200): updated user object in the same shape as the get user response.

---

**DELETE `/api/admin/users/{userId}` — Delete user**

Response (200): confirmation message. Returns `404` if user not found.

---

### Financial Records

#### Admin operations

> Requires **ADMIN** role.

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/admin/records` | Create a financial record |
| `PUT` | `/api/admin/records/{recordId}` | Update a record |
| `DELETE` | `/api/admin/records/{recordId}` | Soft-delete a record |

---

**POST `/api/admin/records` — Create record**

Request body:

| Field | Type | Required | Notes |
|---|---|---|---|
| `amount` | BigDecimal | Yes | ≥ 0, max 10 integer digits + 2 decimal places |
| `type` | String | Yes | `INCOME` or `EXPENSE` |
| `category` | String | Yes | Must belong to the selected `type` (cross-field validation) |
| `recordDate` | LocalDate | Yes | Past or present date (`yyyy-MM-dd`) |
| `description` | String | No | — |

```json
{
  "amount": 15000.50,
  "type": "INCOME",
  "category": "PRODUCT_SALES",
  "recordDate": "2025-03-15",
  "description": "Q1 product sales revenue"
}
```

Response (201):
```json
{
  "id": 42,
  "amount": 15000.50,
  "type": "INCOME",
  "category": "PRODUCT_SALES",
  "recordDate": "2025-03-15",
  "description": "Q1 product sales revenue",
  "createdAt": "2025-03-20T14:30:00"
}
```

---

**PUT `/api/admin/records/{recordId}` — Update record**

All fields are optional. Only provided fields are updated. Same field-level validation as create applies when a field is present.

| Field | Type | Required | Notes |
|---|---|---|---|
| `amount` | BigDecimal | No | ≥ 0, max 10 integer digits + 2 decimal places |
| `type` | String | No | `INCOME` or `EXPENSE` |
| `category` | String | No | Must belong to the selected `type` if both are provided |
| `recordDate` | LocalDate | No | Past or present date (`yyyy-MM-dd`) |
| `description` | String | No | — |

```json
{
  "amount": 18000.00,
  "type": "INCOME",
  "category": "SERVICE_REVENUE",
  "recordDate": "2025-03-20",
  "description": "Updated description"
}
```

Response (200): updated record object in the same shape as the create response.

---

**DELETE `/api/admin/records/{recordId}` — Soft-delete record**

Records are not physically removed. Hibernate `@SQLDelete` / `@SQLRestriction` marks the record as deleted and excludes it from all subsequent queries. Returns `404` if the record does not exist.

---

#### Read operations

> Requires **ANALYST** role or higher.

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/records/{recordId}` | Get a record by ID |
| `GET` | `/api/records` | List records with dynamic filtering, pagination, and sorting |

---

**GET `/api/records/{recordId}` — Get record by ID**

Response (200):
```json
{
  "id": 42,
  "amount": 15000.50,
  "type": "INCOME",
  "category": "PRODUCT_SALES",
  "recordDate": "2025-03-15",
  "description": "Q1 product sales revenue",
  "createdAt": "2025-03-20T14:30:00"
}
```

---

**GET `/api/records` — List records**

Query parameters:

| Parameter | Type | Required | Default | Description |
|---|---|---|---|---|
| `startDate` | LocalDate | No | — | Records on or after this date (`yyyy-MM-dd`) |
| `endDate` | LocalDate | No | — | Records on or before this date |
| `type` | String | No | — | `INCOME` or `EXPENSE` |
| `category` | String | No | — | Filter by category |
| `minAmount` | BigDecimal | No | — | Minimum amount |
| `maxAmount` | BigDecimal | No | — | Maximum amount |
| `page` | Integer | No | `0` | Page number (zero-indexed) |
| `size` | Integer | No | `10` | Page size |
| `sortBy` | String | No | `id` | Sort field (e.g. `amount`, `recordDate`, `category`) |
| `ascending` | Boolean | No | `true` | Sort direction |

---

### Dashboard

> Requires **VIEWER** role or higher.

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/dashboard/total-income` | Total income across all records |
| `GET` | `/api/dashboard/total-expense` | Total expenses across all records |
| `GET` | `/api/dashboard/net-balance` | Net balance (income minus expenses) |
| `GET` | `/api/dashboard/category-wise` | Totals broken down by every category (zero-filled) |
| `GET` | `/api/dashboard/recent-activity` | N most recent financial records |
| `GET` | `/api/dashboard/trends` | Weekly or monthly income vs. expense trends |

---

**GET `/api/dashboard/total-income`**

Response (200):
```json
{ "summaryType": "TOTAL_INCOME", "value": 285000.00 }
```

**GET `/api/dashboard/total-expense`**

Response (200):
```json
{ "summaryType": "TOTAL_EXPENSE", "value": 142000.00 }
```

**GET `/api/dashboard/net-balance`**

Response (200):
```json
{ "summaryType": "NET_BALANCE", "value": 143000.00 }
```

---

**GET `/api/dashboard/category-wise`**

Returns totals for every category, including those with zero activity.

Response (200):
```json
[
  { "category": "PRODUCT_SALES",   "type": "INCOME",  "totalAmount": 120000.00 },
  { "category": "SERVICE_REVENUE", "type": "INCOME",  "totalAmount": 85000.00  },
  { "category": "SALARIES",        "type": "EXPENSE", "totalAmount": 95000.00  },
  { "category": "MARKETING",       "type": "EXPENSE", "totalAmount": 0.00      }
]
```

---

**GET `/api/dashboard/recent-activity`**

Query parameters:

| Parameter | Type | Required | Default | Description |
|---|---|---|---|---|
| `n` | Long | No | `10` | Number of recent records to return (min: 1) |

Response (200): array of record objects in the same shape as `GET /api/records/{recordId}`, ordered by date descending.

---

**GET `/api/dashboard/trends`**

Query parameters:

| Parameter | Type | Required | Default | Description |
|---|---|---|---|---|
| `type` | String | No | `MONTHLY` | `WEEKLY` or `MONTHLY` |
| `n` | Integer | No | `12` (monthly) / `4` (weekly) | Number of periods to return (min: 1) |

Periods with no activity are automatically filled with zero values so the response is always a contiguous sequence.

Response (200):
```json
[
  { "period": "2025-W5", "totalIncome": 12000.00, "totalExpense": 8500.00  },
  { "period": "2025-W6", "totalIncome": 0.00,     "totalExpense": 0.00    },
  { "period": "2025-W7", "totalIncome": 18000.00, "totalExpense": 12000.00 }
]
```

---

## Validation and Error Handling

All request DTOs are validated using Bean Validation. A custom cross-field constraint enforces that `category` belongs to the selected `type`. Errors return a consistent JSON structure across all endpoints:

```json
{
  "status": 400,
  "message": "Category SALARIES is not valid for type INCOME",
  "path": "/api/admin/records",
  "timestamp": "2025-03-20T14:35:00"
}
```

### Categories

Categories are tied to a record type and validated at both the DTO and service level.

**Income:** `PRODUCT_SALES`, `SERVICE_REVENUE`, `CONSULTING_REVENUE`, `SUBSCRIPTION_REVENUE`, `LICENSING_REVENUE`, `INVESTMENT_INCOME`, `INTEREST_INCOME`, `OTHER_INCOME`

**Expense:** `SALARIES`, `OFFICE_RENT`, `UTILITIES`, `SOFTWARE_SUBSCRIPTIONS`, `MARKETING`, `ADVERTISING`, `TRAVEL_EXPENSE`, `EQUIPMENT`, `MAINTENANCE`, `TAX`, `INSURANCE`, `LEGAL_FEES`, `CONSULTING_FEES`, `TRAINING`, `OFFICE_SUPPLIES`, `OTHER_EXPENSE`

### HTTP Status Codes

| Status | When |
|---|---|
| `400 Bad Request` | Validation failures, malformed JSON, type mismatches |
| `401 Unauthorized` | Missing, expired, or invalid JWT |
| `403 Forbidden` | Insufficient role, disabled account |
| `404 Not Found` | User or record not found |
| `409 Conflict` | Duplicate email on user creation |
| `500 Internal Server Error` | Unhandled server errors |

---

## Design Decisions and Assumptions

- **Role hierarchy (ADMIN > ANALYST > VIEWER)** — each higher role inherits all permissions of the roles below it. Implemented via Spring Security granted authorities so no duplicate permission definitions are needed.
- **Soft delete for financial records** — records are never physically removed. Hibernate `@SQLDelete` / `@SQLRestriction` marks them deleted and excludes them from all queries, preserving historical data and audit integrity. Users are hard-deleted as orphaned user data has no business value here.
- **JPA Specifications for filtering** — dynamic query filtering on both users and records is implemented using `JpaSpecificationExecutor` with `UserSpecification` and `FinancialRecordSpecification`. This avoids building query strings manually and keeps filter logic composable and testable.
- **Bean Validation at DTO level** — all input is validated before reaching the service layer. A custom cross-field constraint enforces that `category` belongs to the selected `type`, catching invalid combinations early.
- **Global exception handler** — a single `@RestControllerAdvice` handles all exceptions and returns a consistent JSON error shape (`status`, `message`, `path`, `timestamp`) across every endpoint.
- **JWT for stateless authentication** — no server-side session state. Token expiry is configurable via `security.jwt.expiration-time` (default 6 minutes, intentionally short for the assignment).
- **Pagination on all list endpoints** — all collection responses support `page`, `size`, `sortBy`, and `ascending` parameters so the API remains usable as data grows.
- **Categories as enums mapped to record types** — income and expense categories are defined as Java enums. The mapping between a category and its valid type is enforced at both the DTO and service level, making invalid combinations impossible to persist.

---

## Tradeoffs and Simplifications

- **No refresh tokens** — the current implementation issues a single short-lived JWT with no refresh mechanism. In production, a refresh token flow would be added to avoid forcing re-login on every expiry.
- **No audit logging** — there is no record of who created or modified a financial record. Adding a `createdBy` / `modifiedBy` field backed by Spring Data Auditing would be the natural next step.
- **FinancialRecord has no user ownership** — records are global to the system, not tied to the user who created them. This matches the assignment's multi-role dashboard model but would need revisiting for a multi-tenant system.
- **Schema validated, not auto-generated** — `ddl-auto=validate` means the database schema must be set up manually before first run. This avoids accidental schema drift in shared environments but requires an initial migration step.
- **No rate limiting** — the API has no throttling. This is acceptable for an assignment but would be required before any public deployment.
- **Categories as Enum** — Categories are stored as enums instead of database tables for simplicity.

---

## Getting Started

### Prerequisites

- Java 21+
- MySQL 8.0+
- Maven 3.9+ (or use the included Maven Wrapper)

## Database Setup

1. Run the SQL script provided in the repository (`SQL_SCRIPTS/zorvyn_technical_assignment_script_1.sql`) in your MySQL instance to create the database and tables.

2. Configure the following in `application.properties`:

| Property | Default |
|---|---|
| `spring.datasource.url` | `jdbc:mysql://localhost:3306/zorvyn_ta_db` |
| `spring.datasource.username` | `<YourDbUsername>` |
| `spring.datasource.password` | `<YourDBPassword>` |

3. Run the application — the schema is validated on startup (`ddl-auto=validate`), so the database and tables must already exist before launch.
### Run the Application

```bash
# Using Maven Wrapper
./mvnw spring-boot:run          # Linux / macOS
mvnw.cmd spring-boot:run        # Windows

# Or build and run the JAR
./mvnw clean package -DskipTests
java -jar target/zorvyn_technical_assignment-0.0.1-SNAPSHOT.jar
```

Server starts at `http://localhost:8080` by default.

---

## Project Structure

```
src/main/java/namankhurana/zorvyn_technical_assignment/
│
├── ZorvynTechnicalAssignmentApplication.java
├── config/
├── controller/
├── dto/
├── entity/
├── enums/
├── exception/
├── mapper/
├── repository/
├── security/
├── service/
└── specification/
```
---
## Contact Details

- **Name** — Naman Khurana
- **Email** — namankhurana.dev@gmail.com
- **LinkedIn** — [linkedin.com/in/naman-khurana](https://linkedin.com/in/naman-khurana)
- **GitHub Repo** — [github.com/Naman-Khurana/zorvyn_ta_financial_dashboard_backend](https://github.com/Naman-Khurana/zorvyn_ta_financial_dashboard_backend)