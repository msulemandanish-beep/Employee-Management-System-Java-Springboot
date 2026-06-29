package com.example.employeemanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * EmployeeManagementSystemApplication
 *
 * This is the ENTRY POINT of our Spring Boot application.
 *
 * @SpringBootApplication is a shortcut annotation that combines:
 *   - @Configuration      : marks this class as a source of Spring beans
 *   - @EnableAutoConfiguration : tells Spring Boot to auto-configure based on dependencies
 *   - @ComponentScan      : scans this package and sub-packages for Spring components
 *
 * When you run this class, Spring Boot:
 *   1. Starts an embedded Tomcat server
 *   2. Sets up H2 in-memory database
 *   3. Registers all your REST endpoints
 *   4. Makes the app available at http://localhost:8081
 */
@SpringBootApplication
public class EmployeeManagementSystemApplication {

    public static void main(String[] args) {
        // SpringApplication.run() bootstraps and starts the Spring application
        SpringApplication.run(EmployeeManagementSystemApplication.class, args);
        System.out.println("========================================");
        System.out.println("  Employee Management System Started!  ");
        System.out.println("  App URL  : http://localhost:8081      ");
        System.out.println("  H2 Console: http://localhost:8081/h2-console");
        System.out.println("========================================");
    }

}
