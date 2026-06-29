# Employee Management System
### Spring Boot 3.2.5 | Java 17 | H2 Database | REST API

A beginner-friendly Employee Management System built with Spring Boot.
Perform full **Create, Read, Update, Delete (CRUD)** operations on employees via REST APIs and a simple HTML frontend.

---

## Table of Contents

1. [What is Spring Boot?](#1-what-is-spring-boot)
2. [Tech Stack](#2-tech-stack)
3. [Project Structure](#3-project-structure)
4. [Layer-by-Layer Explanation](#4-layer-by-layer-explanation)
5. [Request Flow](#5-request-flow)
6. [How to Run (VS Code)](#6-how-to-run-in-vs-code)
7. [How to Run (Maven)](#7-how-to-run-using-maven)
8. [How to Access H2 Console](#8-how-to-access-h2-console)
9. [API Documentation](#9-api-documentation)
10. [Validation Rules](#10-validation-rules)
11. [Common Beginner Mistakes](#11-common-beginner-mistakes)

---

## 1. What is Spring Boot?

**Spring Boot** is a Java framework that makes it easy to build web applications and REST APIs.

Without Spring Boot, building a Java web app required:
- Writing hundreds of lines of configuration
- Setting up a separate server (like Tomcat)
- Managing all library versions manually

With Spring Boot, you just:
- Add annotations like `@RestController`, `@Service`, `@Repository`
- Write your business logic
- Run the app — Spring handles everything else automatically

**Spring Boot uses "Convention over Configuration"** — it makes smart default choices so you don't have to configure everything yourself.

---

## 2. Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Spring Boot | 3.2.5 | Web framework |
| Spring Web | Included | Build REST APIs |
| Spring Data JPA | Included | Database access without SQL |
| Spring Validation | Included | Validate request data |
| H2 Database | Included | In-memory database (no install needed) |
| Maven | 3.x | Build tool and dependency management |

---

## 3. Project Structure

```
Employee-Management-System-Java-Springboot/
│
├── pom.xml                                         ← Maven config + dependencies
│
├── EmployeeManagement.postman_collection.json      ← Postman API tests
│
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/example/employeemanagement/
    │   │       │
    │   │       ├── EmployeeManagementSystemApplication.java   ← ENTRY POINT
    │   │       │
    │   │       ├── entity/
    │   │       │   └── Employee.java               ← Database table model
    │   │       │
    │   │       ├── repository/
    │   │       │   └── EmployeeRepository.java     ← Database operations
    │   │       │
    │   │       ├── service/
    │   │       │   ├── EmployeeService.java        ← Service interface
    │   │       │   └── EmployeeServiceImpl.java    ← Business logic
    │   │       │
    │   │       ├── controller/
    │   │       │   └── EmployeeController.java     ← REST API endpoints
    │   │       │
    │   │       └── exception/
    │   │           ├── ResourceNotFoundException.java  ← Custom 404 exception
    │   │           └── GlobalExceptionHandler.java    ← Handles all errors
    │   │
    │   └── resources/
    │       ├── application.properties              ← App configuration
    │       └── static/
    │           ├── index.html                      ← Frontend HTML
    │           ├── style.css                       ← Frontend CSS
    │           └── script.js                       ← Frontend JavaScript
    │
    └── test/
        └── java/
            └── com/example/employeemanagement/
                └── EmployeeManagementSystemApplicationTests.java
```

---

## 4. Layer-by-Layer Explanation

Spring Boot applications are organized into **layers**. Each layer has one job.

### Layer 1: Entity (`entity/Employee.java`)

The **entity** is a Java class that maps to a database table.

```java
@Entity          // "This class = a database table"
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;       // Auto-generated: 1, 2, 3...

    @NotBlank
    private String name;

    @Email
    private String email;
    // ...
}
```

- `@Entity` → tells JPA "create a table for this class"
- `@Id` → this is the primary key
- `@GeneratedValue` → auto-increment the ID
- `@NotBlank`, `@Email` → validation rules

---

### Layer 2: Repository (`repository/EmployeeRepository.java`)

The **repository** talks directly to the database. By extending `JpaRepository`, you get free database methods:

```java
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // You get these for FREE:
    // save(employee)       → INSERT or UPDATE
    // findById(id)         → SELECT WHERE id = ?
    // findAll()            → SELECT all rows
    // deleteById(id)       → DELETE WHERE id = ?
}
```

You don't write SQL. JPA generates it for you.

---

### Layer 3: Service (`service/EmployeeServiceImpl.java`)

The **service** contains your **business logic** — the rules that make your app work.

```java
@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee getEmployeeById(Long id) {
        // Business rule: throw an error if employee doesn't exist
        return employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }
}
```

- `@Service` → Spring registers this as a managed component
- `@Autowired` → Spring automatically provides the repository

---

### Layer 4: Controller (`controller/EmployeeController.java`)

The **controller** receives HTTP requests and returns HTTP responses.

```java
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        Employee saved = employeeService.createEmployee(employee);
        return new ResponseEntity<>(saved, HttpStatus.CREATED); // 201
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK); // 200
    }
}
```

- `@RestController` → this class handles REST requests and returns JSON
- `@RequestMapping` → sets the base URL path
- `@Valid` → triggers validation
- `@RequestBody` → reads JSON from request body
- `@PathVariable` → reads `{id}` from the URL

---

### Layer 5: Exception Handling (`exception/`)

**ResourceNotFoundException** is a custom exception thrown when an employee isn't found.

**GlobalExceptionHandler** catches all exceptions and returns clean JSON error responses instead of ugly stack traces.

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", 404);
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

---

## 5. Request Flow

Here's what happens when you send a request:

```
Postman / Browser (Frontend)
         ↓
   HTTP Request
         ↓
  EmployeeController        ← Receives the request, validates input
         ↓
  EmployeeServiceImpl       ← Applies business rules
         ↓
  EmployeeRepository        ← Executes database query
         ↓
   H2 Database              ← Stores/retrieves data
         ↓
  (same path back up)
         ↓
  HTTP Response (JSON)
         ↓
Postman / Browser (Frontend)
```

**Example — Create Employee:**

1. You send `POST /api/employees` with JSON body
2. `EmployeeController.createEmployee()` receives it
3. `@Valid` checks all validation rules
4. `EmployeeServiceImpl.createEmployee()` calls the repository
5. `EmployeeRepository.save()` inserts a row into H2
6. The saved employee (with ID) is returned as JSON with `201 Created`

---

## 6. How to Run in VS Code

**Prerequisites:**
- Java 17+ installed
- Maven installed (or use `./mvnw`)
- VS Code with "Extension Pack for Java" installed

**Steps:**

1. Open VS Code
2. `File → Open Folder` → select `Employee-Management-System-Java-Springboot`
3. Wait for Java extension to index the project
4. Open `EmployeeManagementSystemApplication.java`
5. Click the **▶ Run** button above the `main` method

Or use the terminal in VS Code:
```bash
mvn spring-boot:run
```

6. Open browser: **http://localhost:8081**

---

## 7. How to Run Using Maven

Open a terminal and navigate to the project root folder:

```bash
# Navigate to project folder
cd Employee-Management-System-Java-Springboot

# Run the application
mvn spring-boot:run
```

You should see output like:
```
Tomcat started on port 8081
Started EmployeeManagementSystemApplication in 2.3 seconds
========================================
  Employee Management System Started!
  App URL  : http://localhost:8081
  H2 Console: http://localhost:8081/h2-console
========================================
```

**Other useful Maven commands:**
```bash
mvn clean install       # Download dependencies and build
mvn test                # Run tests
mvn clean               # Clean build artifacts
```

---

## 8. How to Access H2 Console

H2 is an in-memory database — it runs inside your app, no installation needed.

**To view your database:**

1. Make sure the app is running
2. Open browser: **http://localhost:8081/h2-console**
3. Enter these connection details:
   - **JDBC URL:** `jdbc:h2:mem:employee_db`
   - **Username:** `sa`
   - **Password:** *(leave empty)*
4. Click **Connect**

You'll see the `EMPLOYEES` table. You can run SQL like:
```sql
SELECT * FROM EMPLOYEES;
```

> ⚠️ **Note:** H2 is in-memory, so all data is lost when you restart the app. This is perfect for learning and development.

---

## 9. API Documentation

**Base URL:** `http://localhost:8081`

---

### Create Employee
```
POST /api/employees
```

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "department": "Engineering",
  "position": "Software Developer",
  "salary": 75000
}
```

**Success Response:** `201 Created`
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "department": "Engineering",
  "position": "Software Developer",
  "salary": 75000.0
}
```

---

### Get All Employees
```
GET /api/employees
```

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "department": "Engineering",
    "position": "Software Developer",
    "salary": 75000.0
  }
]
```

---

### Get Employee by ID
```
GET /api/employees/{id}
```

**Example:** `GET /api/employees/1`

**Success Response:** `200 OK` — employee object

**Error Response:** `404 Not Found`
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 1",
  "timestamp": "2024-01-15T10:30:00"
}
```

---

### Update Employee
```
PUT /api/employees/{id}
```

**Request Body:** same as Create

**Success Response:** `200 OK` — updated employee object

**Error Response:** `404 Not Found` if ID doesn't exist

---

### Delete Employee
```
DELETE /api/employees/{id}
```

**Success Response:** `200 OK`
```
Employee deleted successfully
```

**Error Response:** `404 Not Found` if ID doesn't exist

---

## 10. Validation Rules

| Field | Rule | Error Message |
|---|---|---|
| `name` | Required, cannot be blank | Name is required |
| `email` | Required, must be valid email | Email is required / Please provide a valid email address |
| `department` | Required, cannot be blank | Department is required |
| `position` | Required, cannot be blank | Position is required |
| `salary` | Must be greater than 0 | Salary must be greater than 0 |

**Validation Error Response (400 Bad Request):**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Please fix the following errors:",
  "fieldErrors": {
    "name": "Name is required",
    "email": "Please provide a valid email address",
    "salary": "Salary must be greater than 0"
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

---

## 11. Common Beginner Mistakes

### ❌ Mistake 1: Forgetting `@Valid` in the Controller

```java
// WRONG - validation annotations are ignored!
public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) { ... }

// CORRECT - @Valid triggers the validation
public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) { ... }
```

---

### ❌ Mistake 2: Not adding `Content-Type: application/json` header

When sending POST/PUT requests in Postman, always set:
```
Header: Content-Type = application/json
```
Without this, Spring can't read the request body.

---

### ❌ Mistake 3: Confusing HTTP Methods

| Method | Use For |
|---|---|
| GET | Read/Fetch data |
| POST | Create new data |
| PUT | Update existing data |
| DELETE | Remove data |

---

### ❌ Mistake 4: Forgetting `@Autowired`

```java
// WRONG - NullPointerException at runtime!
private EmployeeRepository employeeRepository;

// CORRECT - Spring injects the dependency
@Autowired
private EmployeeRepository employeeRepository;
```

---

### ❌ Mistake 5: Trying to return the ID before saving

```java
// WRONG - id is null before saving!
Employee employee = new Employee();
System.out.println(employee.getId()); // null

// CORRECT - id is assigned AFTER save()
Employee savedEmployee = employeeRepository.save(employee);
System.out.println(savedEmployee.getId()); // 1, 2, 3...
```

---

### ❌ Mistake 6: H2 data disappears after restart

H2 is an **in-memory** database. All data is lost when you stop the app.
This is expected and fine for learning.

For persistent data in production, you'd switch to MySQL or PostgreSQL by changing `application.properties`.

---

### ❌ Mistake 7: Port already in use

If you see:
```
Web server failed to start. Port 8081 was already in use.
```

Either stop the other app using port 8081, or change the port in `application.properties`:
```properties
server.port=8082
```

---

## Postman Collection

Import `EmployeeManagement.postman_collection.json` into Postman:

1. Open Postman
2. Click **Import** → select the JSON file
3. All 7 requests will be pre-configured for `http://localhost:8081`

---

*Built with ❤️ for Spring Boot interns — keep building!*
