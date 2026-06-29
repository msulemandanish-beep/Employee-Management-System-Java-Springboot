package com.example.employeemanagement.service;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * EmployeeServiceImpl
 *
 * This class IMPLEMENTS the EmployeeService interface.
 * It contains the actual business logic for each operation.
 *
 * BUSINESS LOGIC = rules and operations that make our app work.
 * Example: "Before saving an employee, check if the email is already taken."
 *
 * @Service tells Spring this is a service layer component.
 * Spring will automatically create an instance of this class (called a "Bean")
 * and inject it wherever it's needed.
 *
 * @Autowired tells Spring to automatically inject the EmployeeRepository
 * dependency. We don't create it manually - Spring handles it for us.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    // Spring will inject the repository automatically
    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Create a new employee and save to database.
     *
     * @param employee - the Employee object from the request body
     * @return the saved Employee (now has an auto-generated ID)
     */
    @Override
    public Employee createEmployee(Employee employee) {
        // Save the employee to the database and return the saved object
        // The saved object will have the auto-generated ID assigned by the database
        return employeeRepository.save(employee);
    }

    /**
     * Get all employees from the database.
     *
     * @return List of all employees
     */
    @Override
    public List<Employee> getAllEmployees() {
        // findAll() fetches every row from the employees table
        return employeeRepository.findAll();
    }

    /**
     * Get a single employee by their ID.
     * Throws ResourceNotFoundException if not found.
     *
     * @param id - the employee ID to search for
     * @return the found Employee
     */
    @Override
    public Employee getEmployeeById(Long id) {
        // findById() returns an Optional<Employee>
        // orElseThrow() returns the employee if found, or throws our custom exception
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));
    }

    /**
     * Update an existing employee's details.
     *
     * @param id       - the ID of the employee to update
     * @param employee - the new data to update with
     * @return the updated Employee
     */
    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        // First, check if the employee exists - throws exception if not found
        Employee existingEmployee = getEmployeeById(id);

        // Update each field with the new values from the request
        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setPosition(employee.getPosition());
        existingEmployee.setSalary(employee.getSalary());

        // Save and return the updated employee
        // Since existingEmployee already has an ID, JPA will UPDATE (not INSERT)
        return employeeRepository.save(existingEmployee);
    }

    /**
     * Delete an employee by ID.
     * Throws ResourceNotFoundException if not found.
     *
     * @param id - the ID of the employee to delete
     */
    @Override
    public void deleteEmployee(Long id) {
        // First, check if the employee exists - throws exception if not found
        Employee existingEmployee = getEmployeeById(id);

        // Delete the employee from the database
        employeeRepository.delete(existingEmployee);
    }

}
