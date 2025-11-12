# **Todo List API**
**A complete RESTful API for managing tasks (To-dos)**  
Built with **Spring Boot 3**, **Java 17**, **JPA/Hibernate**, **automated testing**, and best practices in clean architecture and code quality.

---

## Technologies Used

| Layer | Technology                                              |
|------|---------------------------------------------------------|
| **Language** | Java 17                                                 |
| **Framework** | Spring Boot 3.4.0+                                      |
| **Web** | Spring Web MVC                                          |
| **Persistence** | Spring Data JPA + Hibernate                             |
| **Database** | MySQL (local)                                           |
| **Validation** | Jakarta Bean Validation (`@Valid`, `@NotNull`, `@Size`) |
| **Documentation** | SpringDoc OpenAPI + Swagger UI                          |
| **Build Tool** | Maven                                                   |
| **Architecture** | Layered (Controller → Service → Repository)             |
| **Immutability** | Records (DTOs)                                          |

---

## Features (Full CRUD)

| Method | Endpoint | Description | HTTP Status                                |
|--------|---------|-------------|--------------------------------------------|
| `POST` | `/todo` | Create a new task | `201 Created` / `422 Unprocessable Entity` |
| `GET` | `/todo` | List all tasks | `200 OK`  / `422 Unprocessable Entity`                               |
| `GET` | `/todo/{id}` | Get task by ID | `200 OK` / `404 Not Found`                 |
| `PUT` | `/todo` | Update task (ID in body) | `200 OK` / `404 Not Found`                 |
| `DELETE` | `/todo/{id}` | Delete task | `204 No Content` / `404 Not Found`         |

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
└── TodoListApiApplication.java
```

---

## Best Practices Applied

| Practice | Implementation |
|---------|----------------|
| **Layered Architecture** | Controller → Service → Repository |
| **Specific DTOs** | `CreateRequest` ≠ `UpdateRequest` |
| **Input Validation** | `@Valid`, `@NotNull`, `@Size`, `@Min` |
| **Centralized Error Handling** | `@ControllerAdvice` + `ErrorResponse` |
| **RESTful Responses** | Correct status codes, `Location` header on `POST` |
| **Immutability** | `record` for DTOs |
| **Optional + orElseGet** | Avoids `NullPointerException` |
| **Automated Documentation** | Swagger UI at `/swagger-ui.html` |

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.8+

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/luizgdsmdev/java-challenges.git
cd java-challenges/todo-list-api

# 2. Build and run
mvn spring-boot:run
```

> The API will be available at:  
> **http://localhost:8080**

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

### 2. List All Tasks
```bash
curl http://localhost:8080/todo
```

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

> Test all endpoints directly in the browser.

---

## Error Handling (Standardized)

```json
{
  "message": "Invalid data",
  "details": "title: Title must be between 3 and 60 characters",
  "timestamp": "2025-11-11T12:00:00.000"
}
```

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
    @NotNull Boolean completed,
    @Min(1) @Max(5) Integer priority,
    String title,
    String description
)
```

---

## Author

**Luiz Messias**  
[GitHub: @luizgdsmdev](https://github.com/luizgdsmdev)  
[LinkedIn: @luizgdsm](https://www.linkedin.com/in/luizgdsm/)

> Developed as a technical challenge (listed [here](https://github.com/simplify-tec/desafio-junior-backend-simplify)) to demonstrate **proficiency in Spring Boot**, **clean architecture**, **validation**, and **REST best practices**.

---

## License

MIT License – Free to use for study, portfolio, or improvement.

---