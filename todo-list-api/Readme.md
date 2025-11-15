# **To-do List API**

**A complete RESTful API for managing tasks (To-dos)**  
Built with **Spring Boot 3**, **Java 17**, **JPA/Hibernate**, **automated testing**, and best practices in clean architecture and code quality.

---

## Technologies Used

| Layer             | Technology                                              |
| ----------------- | ------------------------------------------------------- |
| **Language**      | Java 17                                                 |
| **Framework**     | Spring Boot 3.4.0+                                      |
| **Web**           | Spring Web MVC                                          |
| **Persistence**   | Spring Data JPA + Hibernate                             |
| **Database**      | MySQL (prod) – H2 in-memory (tests)                     |
| **Validation**    | Jakarta Bean Validation (`@Valid`, `@NotNull`, `@Size`) |
| **Documentation** | SpringDoc OpenAPI + Swagger UI                          |
| **Build Tool**    | Maven                                                   |
| **Architecture**  | Layered (Controller → Service → Repository)             |
| **Immutability**  | Records (DTOs)                                          |
| **Testing**       | JUnit 5, WebTestClient, H2, `@ActiveProfiles("test")`   |

---

## Features (Full CRUD + Pagination)

| Method   | Endpoint     | Description                | HTTP Status                                |
| -------- | ------------ | -------------------------- | ------------------------------------------ |
| `POST`   | `/todo`      | Create a new task          | `201 Created` / `422 Unprocessable Entity` |
| `GET`    | `/all`       | List all tasks – paginated | `200 OK` / `422 Unprocessable Entity`      |
| `GET`    | `/todo/{id}` | Get task by ID             | `200 OK` / `404 Not Found`                 |
| `PUT`    | `/todo`      | Update task (ID in body)   | `200 OK` / `404 Not Found`                 |
| `DELETE` | `/todo/{id}` | Delete task                | `204 No Content` / `404 Not Found`         |

> **New in `/all`**: Pagination, sorting, and **max page size = 100** (enforced via `PageableHandlerMethodArgumentResolver`).

---

## Project Structure

```
src/main/java/com/todoapirest/todo_list_api/
├── Controller/
│   └── TodoController.java
├── Entity/
│   └── Todo.java
├── Exception/
│   ├── Records/
│   │   └── ErrorResponse.java
│   └── GlobalExceptionHandler.java
├── Repository/
│   └── TodoRepository.java
├── Service/
│   └── TodoService.java
├── TodoDataTransferObject/
│   ├── TodoCreateRequest.java
│   └── TodoUpdateRequest.java
├── Config/
│   └── PageableConfig.java
└── TodoListApiApplication.java
```

---

## Best Practices Applied

| Practice                       | Implementation                                           |
| ------------------------------ | -------------------------------------------------------- |
| **Layered Architecture**       | Controller → Service → Repository                        |
| **Specific DTOs**              | `CreateRequest` ≠ `UpdateRequest`                        |
| **Input Validation**           | `@Valid`, `@NotNull`, `@Size`, `@Min`                    |
| **Centralized Error Handling** | `@ControllerAdvice` + `ErrorResponse`                    |
| **RESTful Responses**          | Correct status codes, `Location` header on `POST`        |
| **Immutability**               | `record` for DTOs                                        |
| **Optional + orElseGet**       | Avoids `NullPointerException`                            |
| **Pagination Security**        | `maxPageSize = 100` enforced globally                    |
| **Test Isolation**             | H2 in-memory + `@ActiveProfiles("test")` + `deleteAll()` |
| **Automated Documentation**    | Swagger UI at `/swagger-ui.html`                         |

---

## How to Run

### Prerequisites

- Java 17+
- Maven 3.925+

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/luizgdsmdev/java-challenges.git
cd java-challenges/todo-list-api

# 2. Build and run (production – MySQL)
mvn spring-boot:run
```

> The API will be available at:  
> **http://localhost:8080**

### Run Tests (H2 in-memory)

```bash
mvn test
```

> Uses `application-test.properties` with H2, isolated from production.

---

## Endpoints with Examples

### 1. Create Task

```bash
curl -X POST http://localhost:8080/todo \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Study Spring Boot",
    "completed": false,
    "description": "Review controllers and services",
    "priority": 1
  }'
```

**Response:**

```json
{
  "id": 1,
  "title": "Study Spring Boot",
  "completed": false,
  "description": "Review controllers and services",
  "priority": 1
}
```

> `Location: http://localhost:8080/todo/1`

---

### 2. List All Tasks – **Paginated**

```bash
curl "http://localhost:8080/todo/all?page=0&size=5&sort=priority,desc"
```

**Response:**

```json
{
  "content": [
    {
      "id": 5,
      "title": "Urgent Task",
      "completed": false,
      "description": "High priority",
      "priority": 5
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 5,
    "sort": { "sorted": true, "unsorted": false }
  },
  "totalElements": 26,
  "totalPages": 6,
  "first": true,
  "last": false,
  "numberOfElements": 5
}
```

> **Query Parameters**:
>
> - `page` – page number (0-indexed), default: `0`
> - `size` – items per page, **max 100**, default: `20`
> - `sort` – `property,asc|desc` (e.g. `title,asc`)

---

### 3. Get by ID

```bash
curl http://localhost:8080/todo/1
```

---

### 4. Update Task

```bash
curl -X PUT http://localhost:8080/todo \
  -H "Content-Type: application/json" \
  -d '{
    "id": 1,
    "title": "Study Spring Boot - Done",
    "completed": true
  }'
```

---

### 5. Delete Task

```bash
curl -X DELETE http://localhost:8080/todo/1
```

---

## Interactive Documentation

Access the Swagger UI:  
**http://localhost:8080/swagger-ui.html**

> Test all endpoints directly in the browser (pagination parameters included).

---

## Error Handling (Standardized)

```json
{
  "title": "Unprocessable Entity",
  "message": "Malformed JSON in request body or invalid/non-existent id",
  "timestamp": "2025-11-15T16:57:00.000"
}
```

---

## Data Structure

### `TodoCreateRequest`

```java
record TodoCreateRequest(
    @NotNull @Size(min=3, max=60) String title,
    @NotNull Boolean completed,
    @Min(1) @Max(5) Integer priority,
    String description
)
```

### `TodoUpdateRequest`

```java
record TodoUpdateRequest(
    @NotNull @Min(1) Long id,
    @NotNull @Size(min=3, max=60) String title,
    @NotNull Boolean completed,
    @Min(1) @Max(5) Integer priority,
    String description
)
```

---

## Testing Strategy

- **Integration Tests**: `WebTestClient` + real controller + H2 in-memory
- **Isolation**: `@ActiveProfiles("test")`, `application-test.properties`, `deleteAll()` in `@BeforeEach`
- **Coverage**:
  - `GET /all` with pagination (default, custom, max size)
  - `GET /todo/{id}` success & 404
  - Error handling (422, 404)
  - Data consistency (`totalElements`, `totalPages`)

---

## Author

**Luiz Messias**  
[GitHub: @luizgdsmdev](https://github.com/luizgdsmdev)  
[LinkedIn: @luizgdsm](https://www.linkedin.com/in/luizgdsm/)

> Developed as a technical challenge (listed [here](https://github.com/simplify-tec/desafio-junior-backend-simplify)) to demonstrate **proficiency in Spring Boot**, **clean architecture**, **validation**, **pagination**, **testing**, and **REST best practices**.

---

## License

MIT License – Free to use for study, portfolio, or improvement.

---
