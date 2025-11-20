# **Itaú API REST Challenge**

**A complete RESTful API for managing transactions with statistics**  
Built with **Spring Boot 3**, **Java 17**, **JPA/Hibernate**, **automated testing**, and best practices in clean architecture, validation, and error handling.

---

## Technologies Used

| Layer             | Technology                                             |
| ----------------- | ------------------------------------------------------ |
| **Language**      | Java 17                                                |
| **Framework**     | Spring Boot 3.4.0+                                     |
| **Web**           | Spring Web MVC                                         |
| **Persistence**   | In-Memory List (simplified, no JPA for challenge)      |
| **Database**      | In-Memory (List-based), as requirement                 |
| **Validation**    | Jakarta Bean Validation (`@Valid`, `@NotNull`, `@Min`) |
| **Documentation** | SpringDoc OpenAPI + Swagger UI                         |
| **Build Tool**    | Maven                                                  |
| **Architecture**  | Layered (Controller → Service → Model)                 |
| **Immutability**  | Records (DTOs)                                         |

---

## Features (CRUD + Statistics)

| Method   | Endpoint       | Description                                                 | HTTP Status                                                    |
| -------- | -------------- | ----------------------------------------------------------- | -------------------------------------------------------------- |
| `POST`   | `/transacao`   | Create a new transaction                                    | `201 Created` / `400 Bad Request` / `422 Unprocessable Entity` |
| `GET`    | `/estatistica` | Get statistics (count, sum, min, max, average) for last 60s | `200 OK` / `400 Bad Request`                                   |
| `DELETE` | `/transacao`   | Clear all transactions                                      | `200 OK` / `400 Bad Request` / `503 Service`                   |

> **Key Rules**: Transactions older than 60 seconds are ignored in statistics. Values must be >= 0. Dates cannot be in the future.

---

## Project Structure

```
src/main/java/com/itau/bank/backend/itau_API_REST_challenge/
├── controller/
│   └── TransactionalController.java
├── dto/
│   └── TransactionalRequest.java
├── model/
│   └── Transaction.java
├── service/
│   └── TransactionalService.java
├── exception/
│   ├── BusinessException.java
│   └── GlobalExceptionHandler.java
├── utils/
│   └── validations/
│       └── TransactionalControllerValidations.java
└── ItauApiRestChallengeApplication.java
```

---

## Best Practices Applied

| Practice                       | Implementation                                                        |
| ------------------------------ | --------------------------------------------------------------------- |
| **Layered Architecture**       | Controller → Service → Model                                          |
| **Input Validation**           | `@Valid`, `@NotNull`, `@Min(0)` on DTOs                               |
| **Centralized Error Handling** | `@ControllerAdvice` + `BusinessException` + ErrorResponse             |
| **RESTful Responses**          | Correct status codes (201, 400, 422, 503), empty body on create       |
| **Immutability**               | DTOs                                                                  |
| **Business Rules**             | 60-second window for statistics, no future dates, non-negative values |
| **Test Isolation**             | In-Memory (List-based)                                                |
| **Automated Documentation**    | Swagger UI at `/swagger-ui.html`                                      |

---

## How to Run

### Prerequisites

- Java 17+
- Maven 3.8+

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/luizgdsmdev/java-challenges.git
cd java-challenges/itau_API_REST_challenge

# 2. Build and run
mvn spring-boot:run
```

> The API will be available at:  
> **http://localhost:8080**

### Run Tests (H2 in-memory)

```bash
mvn test
```

> Uses `application-test.properties` with in-memory storage, isolated from production.

---

## Endpoints with Examples

### 1. Create Transaction

```bash
curl -X POST http://localhost:8080/transacao \
  -H "Content-Type: application/json" \
  -d '{
    "valor": 100.50,
    "dataHora": "2025-11-19T10:30:00.123456789-03:00"
  }'
```

**Response (201 Created):**

```
Empty body
```

> No content returned on success.

**Error Example (400 Bad Request - Negative Value):**

```json
{
  "message": "Invalid data",
  "details": "valor: deve ser maior que ou igual à 0",
  "timestamp": "2025-11-19T10:30:00.123"
}
```

**Error Example (422 Unprocessable Entity - Future Date):**

```json
{
  "message": "Unprocessable Entity",
  "details": "dataHora field can't be in future time: 2026-11-19T10:30:00.123456789Z",
  "timestamp": "2025-11-19T10:30:00.123"
}
```

---

### 2. Get Statistics

```bash
curl http://localhost:8080/estatistica
```

**Response (200 OK - Empty):**

```json
{
  "count": 0,
  "sum": 0.0,
  "min": "Infinity",
  "max": "-Infinity",
  "average": 0.0
}
```

**Response (200 OK - With Data):**

```json
{
  "count": 3,
  "sum": 250.5,
  "min": 50.0,
  "max": 100.5,
  "average": 83.5
}
```

> Only includes transactions from the last 60 seconds.

---

### 3. Clear Transactions

```bash
curl -X DELETE http://localhost:8080/transacao
```

**Response (200 OK):**

```
Empty body
```

---

## Interactive Documentation

Access the Swagger UI:  
**http://localhost:8080/swagger-ui.html**

> Test all endpoints directly in the browser.

---

## Error Handling (Standardized)

```json
{
  "message": "Invalid data",
  "details": "valor: deve ser maior que ou igual à 0",
  "timestamp": "2025-11-19T10:30:00.123"
}
```

> All errors use consistent `ErrorResponse` format with timestamps.

## Data Structure

### `TransactionalRequest`

```java
public class TransactionalRequest {
    @NotNull @Min(0)
    private Double valor;

    @NotNull
    private OffsetDateTime dataHora;

    // getters and setters
}
```

---

## Testing Strategy

- **Integration Tests**: `TestRestTemplate` + real controller + in-memory service
- **Isolation**: `@ActiveProfiles("test")`, `application-test.properties`, clear transactions in `@BeforeEach`
- **Coverage**:
  - `POST /transacao` success (201 empty body)
  - `POST /transacao` validation failures (400 for negative value, 422 for future date)
  - `GET /estatistica` empty state (count=0, Infinity)
  - `GET /estatistica` with data (sum, min, max, average correct)
  - `DELETE /transacao` clears data (resets statistics)
  - Edge cases: null fields, invalid formats

---

## Author

**Luiz Messias**  
[GitHub: @luizgdsmdev](https://github.com/luizgdsmdev)  
[LinkedIn: @luizgdsm](https://www.linkedin.com/in/luizgdsm/)

> Developed as a technical challenge for Itaú to demonstrate **proficiency in Spring Boot**, **validation**, **error handling**, **statistics calculation**, **testing**, and **REST best practices**.

---

## License

MIT License – Free to use for study, portfolio, or improvement.

---
