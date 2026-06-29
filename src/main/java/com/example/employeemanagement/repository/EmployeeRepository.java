package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EmployeeRepository
 *
 * This interface handles all database operations for Employee.
 *
 * By extending JpaRepository, we get these methods FOR FREE (no code needed!):
 *
 *   save(employee)           - INSERT or UPDATE an employee
 *   findById(id)             - SELECT * FROM employees WHERE id = ?
 *   findAll()                - SELECT * FROM employees
 *   deleteById(id)           - DELETE FROM employees WHERE id = ?
 *   existsById(id)           - Check if a record exists
 *   count()                  - Count total employees
 *
 * JpaRepository<Employee, Long> means:
 *   - Employee = the entity/table we're working with
 *   - Long     = the data type of the primary key (id)
 *
 * @Repository tells Spring this is a data access layer component
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Spring Data JPA auto-generates the SQL for this method just from the name!
    // It translates to: SELECT * FROM employees WHERE email = ?
    boolean existsByEmail(String email);

}
