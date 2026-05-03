# E-commerce Order Processing System

A scalable backend system that simulates an e-commerce order processing pipeline — handling order lifecycle management, configurable fee calculation, secure API access, and role-based authorization.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 4 |
| ORM | Spring Data JPA (Hibernate) |
| Database | PostgreSQL |
| Auth | JWT (custom, no Spring Security) |
| Build | Gradle |

---

## Getting Started

### Prerequisites
- Java 17
- PostgreSQL
- Gradle

### 1. Clone the repository
```bash
git clone <your-repo-url>
cd ecommerce-application
```

### 2. Create the database
```sql
CREATE DATABASE "ecommerce website";
```

### 3. Set up local configuration

Create the file `src/main/resources/application-local.properties` — this file is ignored by git and holds your sensitive credentials.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce website
spring.datasource.username=your_db_username

jwt.secret=your_base64_encoded_secret
```

**Generate a JWT secret:**
```bash
openssl rand -base64 32
```
Copy the output and paste it as the value for `jwt.secret`.

> `application-local.properties` is listed in `.gitignore` — never commit this file.

### 4. Run the application
```bash
./gradlew bootRun
```

The server starts at `http://localhost:8080`.

---

## Authentication

All `/api/**` endpoints require a valid JWT access token except `/api/auth/**`.

**Step 1 — Register**
```
POST /api/auth/register
```
```json
{
  "name": "Kavya",
  "email": "kavya@example.com",
  "password": "Secret@123",
  "role": "CUSTOMER"
}
```

**Step 2 — Login**
```
POST /api/auth/login
```
```json
{
  "email": "kavya@example.com",
  "password": "Secret@123"
}
```
Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2g...",
  "expiresIn": 3600
}
```

**Step 3 — Use the token**

Add to every request header:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Refresh an expired token**
```
POST /api/auth/refresh
```
```json
{
  "refreshToken": "dGhpcyBpcyBhIHJlZnJlc2g..."
}
```

**Token expiry:**
- Access token — 1 hour
- Refresh token — 7 days

---

## Role-Based Access

| Role | Permissions |
|------|------------|
| `CUSTOMER` | Register, login, place orders, view products, view own orders |
| `ADMIN` | Everything above + create/update/delete products, manage delivery partners, update order status, view all orders |

---

## API Reference

Base URL: `http://localhost:8080/api`

---

### Customers

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/customers` | All | Create a customer |
| `GET` | `/api/customers/{id}` | All | Get customer by ID |
| `PUT` | `/api/customers/{id}` | All | Update customer |
| `GET` | `/api/customers/{id}/orders` | All | Customer's order history |

---

### Products

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/products` | Admin | Create a product |
| `GET` | `/api/products` | All | List all products |
| `GET` | `/api/products/{id}` | All | Get product by ID |
| `PUT` | `/api/products/{id}` | Admin | Update product |
| `DELETE` | `/api/products/{id}` | Admin | Delete product |

---

### Delivery Partners

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/delivery-partners` | Admin | Register a partner |
| `GET` | `/api/delivery-partners/{id}` | All | Get partner by ID |
| `PUT` | `/api/delivery-partners/{id}` | Admin | Update partner info |
| `GET` | `/api/delivery-partners/{id}/orders` | All | Orders assigned to partner |

---

### Orders

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/orders` | All | Place a new order |
| `GET` | `/api/orders/{id}` | All | Get order with items |
| `GET` | `/api/orders?status=CREATED` | Admin | List all orders (filter by status) |
| `PATCH` | `/api/orders/{id}/status` | Admin | Update order status |

**Place an order — request:**
```json
{
  "customerId": 1,
  "deliveryPartnerId": 2,
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 3, "quantity": 1 }
  ]
}
```

**Valid status transitions:**
```
CREATED → CONFIRMED → PACKED → OUT_FOR_DELIVERY → DELIVERED
Any non-terminal state → CANCELLED
```

---

### Fee Calculation

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `GET` | `/api/orders/{id}/fees` | All | Full fee breakdown for an order |

**Fee rules:**

| Component | Formula |
|-----------|---------|
| Tax | `(unitPrice × quantity) × 18%` |
| Platform charge | `(unitPrice × quantity) × 2%` |
| Convenience fee | flat ₹10.00 per line item |
| Total fee | `tax + platform charge + convenience fee` |
| Total amount | `(unitPrice × quantity) − discount + total fee` |

---

## Error Responses

| HTTP Code | Scenario |
|-----------|----------|
| `400` | Missing required field or invalid input |
| `401` | Missing or invalid JWT token |
| `403` | Insufficient role/permissions |
| `404` | Resource not found |
| `409` | Invalid status transition |
| `500` | Unexpected server error |

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Product not found for id: 99"
}
```

---

## Future Enhancements
- Event-driven architecture using Kafka
- Scheduler for automated reconciliation
- Retry mechanisms for failed transactions
- Advanced reporting dashboard