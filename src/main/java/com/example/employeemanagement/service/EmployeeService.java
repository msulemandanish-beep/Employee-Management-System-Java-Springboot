package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.Employee;

import java.util.List;

/**
 * EmployeeService Interface
 *
 * This interface defines WHAT our service can do (the contract).
 * The actual implementation is in EmployeeServiceImpl.java.
 *
 * WHY USE AN INTERFACE?
 * - Separates "what to do" from "how to do it"
 * - Makes the code easier to test (we can swap implementations)
 * - Follows best practices in professional Java development
 *
 * Think of it like a menu at a restaurant:
 * - The interface = the menu (lists what's available)
 * - The implementation = the kitchen (does the actual cooking)
 */
public interface EmployeeService {

    // Create a new employee
    Employee createEmployee(Employee employee);

    // Get all employees
    List<Employee> getAllEmployees();

    // Get a single employee by ID
    Employee getEmployeeById(Long id);

    // Update an existing employee
    Employee updateEmployee(Long id, Employee employee);

    // Delete an employee by ID
    void deleteEmployee(Long id);

}
