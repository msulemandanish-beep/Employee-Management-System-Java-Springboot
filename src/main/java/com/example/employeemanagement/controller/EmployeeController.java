package com.example.employeemanagement.controller;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * EmployeeController
 *
 * This is the REST API layer. It receives HTTP requests and sends HTTP responses.
 *
 * KEY ANNOTATIONS EXPLAINED:
 *
 * @RestController  - Combines @Controller + @ResponseBody
 *                   Tells Spring this class handles REST API requests
 *                   and returns JSON automatically.
 *
 * @RequestMapping  - Sets the BASE URL for all endpoints in this controller.
 *                   All our endpoints start with "/api/employees"
 *
 * @CrossOrigin     - Allows our HTML frontend (running on a different port)
 *                   to call these APIs. Without this, the browser blocks the request.
 *
 * HTTP METHOD ANNOTATIONS:
 * @PostMapping     - Handles HTTP POST requests   (CREATE)
 * @GetMapping      - Handles HTTP GET requests    (READ)
 * @PutMapping      - Handles HTTP PUT requests    (UPDATE)
 * @DeleteMapping   - Handles HTTP DELETE requests (DELETE)
 *
 * @Valid           - Triggers validation annotations on the Employee object
 * @RequestBody     - Reads the JSON from the request body and converts to Employee object
 * @PathVariable    - Reads a value from the URL (e.g., /api/employees/{id})
 *
 * ResponseEntity   - Lets us control the HTTP status code + response body
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    // Spring injects the service automatically
    @Autowired
    private EmployeeService employeeService;

    /**
     * CREATE Employee
     * POST /api/employees
     *
     * Request Body (JSON):
     * {
     *   "name": "John Doe",
     *   "email": "john@example.com",
     *   "department": "Engineering",
     *   "position": "Developer",
     *   "salary": 75000
     * }
     *
     * Response: 201 Created + the saved employee with ID
     */
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        // @Valid triggers all validation annotations (@NotBlank, @Email, @Positive)
        // If validation fails, Spring throws MethodArgumentNotValidException
        // which our GlobalExceptionHandler catches and returns a 400 response

        Employee savedEmployee = employeeService.createEmployee(employee);

        // Return 201 Created with the saved employee in the response body
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    /**
     * GET ALL Employees
     * GET /api/employees
     *
     * Response: 200 OK + list of all employees
     */
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();

        // Return 200 OK with the list of employees
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    /**
     * GET Employee by ID
     * GET /api/employees/{id}
     *
     * Example: GET /api/employees/1
     *
     * Response: 200 OK + the employee
     *           404 Not Found if employee doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        // @PathVariable extracts the {id} from the URL
        Employee employee = employeeService.getEmployeeById(id);

        // Return 200 OK with the found employee
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    /**
     * UPDATE Employee
     * PUT /api/employees/{id}
     *
     * Example: PUT /api/employees/1
     *
     * Request Body (JSON): updated employee data
     *
     * Response: 200 OK + the updated employee
     *           404 Not Found if employee doesn't exist
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id,
                                                    @Valid @RequestBody Employee employee) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);

        // Return 200 OK with the updated employee
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    /**
     * DELETE Employee
     * DELETE /api/employees/{id}
     *
     * Example: DELETE /api/employees/1
     *
     * Response: 200 OK + success message
     *           404 Not Found if employee doesn't exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);

        // Return 200 OK with a confirmation message
        return new ResponseEntity<>("Employee deleted successfully", HttpStatus.OK);
    }

}
