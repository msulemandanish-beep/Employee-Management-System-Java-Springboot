package com.example.employeemanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

/**
 * Employee Entity
 *
 * This class represents the "Employee" table in our H2 database.
 *
 * KEY ANNOTATIONS EXPLAINED:
 *
 * @Entity       - Tells JPA that this class maps to a database table
 * @Table        - Specifies the table name in the database
 * @Id           - Marks the primary key field
 * @GeneratedValue - Auto-increments the ID (1, 2, 3, ...)
 * @Column       - Maps a field to a specific database column
 *
 * VALIDATION ANNOTATIONS:
 * @NotBlank     - Field cannot be null or empty string
 * @Email        - Field must be a valid email format
 * @Positive     - Number must be greater than 0
 */
@Entity
@Table(name = "employees")
public class Employee {

    // Primary Key - auto-generated (1, 2, 3, ...)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Employee name - required field
    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    // Employee email - required + must be valid email format
    @NotBlank(message = "Email is required")
    @Email(message = "Please provide a valid email address")
    @Column(nullable = false, unique = true)
    private String email;

    // Department the employee belongs to
    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    // Employee's job position/title
    @NotBlank(message = "Position is required")
    @Column(nullable = false)
    private String position;

    // Employee salary - must be greater than 0
    @Positive(message = "Salary must be greater than 0")
    @Column(nullable = false)
    private Double salary;

    // =============================================
    // Constructors
    // =============================================

    // Default constructor - required by JPA
    public Employee() {
    }

    // Constructor with all fields (except id - that's auto-generated)
    public Employee(String name, String email, String department, String position, Double salary) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }

    // =============================================
    // Getters and Setters
    // Spring uses these to read/write field values
    // =============================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    // toString() - useful for debugging/logging
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }
}
