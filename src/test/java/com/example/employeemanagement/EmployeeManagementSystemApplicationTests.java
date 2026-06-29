package com.example.employeemanagement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * EmployeeManagementSystemApplicationTests
 *
 * This is a basic test to check that the Spring application context loads correctly.
 *
 * @SpringBootTest loads the full Spring application context for testing.
 * If this test passes, it means all your beans (controllers, services, repositories)
 * are correctly configured.
 *
 * Run tests with: mvn test
 */
@SpringBootTest
class EmployeeManagementSystemApplicationTests {

    @Test
    void contextLoads() {
        // This test verifies that the Spring Boot application starts without errors.
        // If there's a configuration mistake, this test will fail and tell you.
        System.out.println("Spring Boot context loaded successfully!");
    }

}
