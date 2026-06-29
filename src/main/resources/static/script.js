/**
 * script.js - Employee Management System Frontend
 *
 * This file handles all communication between the HTML page and the Spring Boot backend.
 * We use the browser's built-in fetch() API to make HTTP requests.
 *
 * HOW IT WORKS:
 * 1. User fills out the form and clicks "Add Employee"
 * 2. JavaScript calls the Spring Boot REST API using fetch()
 * 3. Spring Boot saves the employee to H2 database
 * 4. Spring Boot returns the saved employee as JSON
 * 5. JavaScript adds the new employee to the table
 */

// The base URL of our Spring Boot backend
const API_URL = "http://localhost:8083/api/employees";

// =============================================
// LOAD ALL EMPLOYEES WHEN PAGE LOADS
// =============================================

// This runs automatically when the page finishes loading
window.onload = function () {
    loadAllEmployees();
};

// =============================================
// FETCH ALL EMPLOYEES FROM BACKEND
// =============================================

function loadAllEmployees() {
    // GET /api/employees
    fetch(API_URL)
        .then(function (response) {
            return response.json(); // Convert response to JSON
        })
        .then(function (employees) {
            renderEmployeeTable(employees); // Display in table
        })
        .catch(function (error) {
            console.error("Error loading employees:", error);
            showAlert("Could not connect to the server. Is Spring Boot running?", "error");
        });
}

// =============================================
// RENDER EMPLOYEES IN THE TABLE
// =============================================

function renderEmployeeTable(employees) {
    const tbody = document.getElementById("employee-tbody");
    const countBadge = document.getElementById("employee-count");

    // Update the count badge
    countBadge.textContent = employees.length + " employee" + (employees.length !== 1 ? "s" : "");

    // If no employees, show the "no data" row
    if (employees.length === 0) {
        tbody.innerHTML = `
            <tr id="no-data-row">
                <td colspan="7" class="no-data">No employees found. Add one above!</td>
            </tr>
        `;
        return;
    }

    // Build the table rows from the employees array
    let rowsHTML = "";

    employees.forEach(function (employee) {
        rowsHTML += `
            <tr>
                <td>${employee.id}</td>
                <td>${employee.name}</td>
                <td>${employee.email}</td>
                <td>${employee.department}</td>
                <td>${employee.position}</td>
                <td>$${employee.salary.toLocaleString()}</td>
                <td>
                    <button class="btn-edit" onclick="loadEmployeeForEdit(${employee.id})">Edit</button>
                    <button class="btn-delete" onclick="deleteEmployee(${employee.id})">Delete</button>
                </td>
            </tr>
        `;
    });

    tbody.innerHTML = rowsHTML;
}

// =============================================
// SUBMIT FORM (CREATE OR UPDATE)
// =============================================

function submitForm() {
    // Get the value from the hidden ID field
    const employeeId = document.getElementById("employee-id").value;

    // If ID exists -> UPDATE, otherwise -> CREATE
    if (employeeId) {
        updateEmployee(employeeId);
    } else {
        createEmployee();
    }
}

// =============================================
// CREATE EMPLOYEE
// POST /api/employees
// =============================================

function createEmployee() {
    // Read values from the form
    const employee = getFormData();

    // Basic client-side check (server also validates)
    if (!validateForm(employee)) return;

    // Call the backend API
    fetch(API_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json" // Tell server we're sending JSON
        },
        body: JSON.stringify(employee) // Convert JS object to JSON string
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(function (err) {
                    throw err; // Pass error to catch block
                });
            }
        })
        .then(function (savedEmployee) {
            showAlert("Employee '" + savedEmployee.name + "' added successfully!", "success");
            clearForm();
            loadAllEmployees(); // Refresh the table
        })
        .catch(function (error) {
            displayServerErrors(error);
        });
}

// =============================================
// LOAD EMPLOYEE DATA INTO FORM FOR EDITING
// GET /api/employees/{id}
// =============================================

function loadEmployeeForEdit(id) {
    // Fetch the employee data from backend
    fetch(API_URL + "/" + id)
        .then(function (response) {
            return response.json();
        })
        .then(function (employee) {
            // Populate the form with this employee's data
            document.getElementById("employee-id").value = employee.id;
            document.getElementById("name").value = employee.name;
            document.getElementById("email").value = employee.email;
            document.getElementById("department").value = employee.department;
            document.getElementById("position").value = employee.position;
            document.getElementById("salary").value = employee.salary;

            // Update the form title and button text
            document.getElementById("form-title").textContent = "Edit Employee";
            document.getElementById("submit-btn").textContent = "Update Employee";
            document.getElementById("submit-btn").style.backgroundColor = "#3498db";
            document.getElementById("cancel-btn").style.display = "inline-block";

            // Scroll to the top of the page so user can see the form
            window.scrollTo({ top: 0, behavior: "smooth" });
        })
        .catch(function (error) {
            showAlert("Could not load employee details.", "error");
        });
}

// =============================================
// UPDATE EMPLOYEE
// PUT /api/employees/{id}
// =============================================

function updateEmployee(id) {
    const employee = getFormData();

    if (!validateForm(employee)) return;

    fetch(API_URL + "/" + id, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(employee)
    })
        .then(function (response) {
            if (response.ok) {
                return response.json();
            } else {
                return response.json().then(function (err) {
                    throw err;
                });
            }
        })
        .then(function (updatedEmployee) {
            showAlert("Employee '" + updatedEmployee.name + "' updated successfully!", "success");
            cancelEdit(); // Reset the form
            loadAllEmployees(); // Refresh the table
        })
        .catch(function (error) {
            displayServerErrors(error);
        });
}

// =============================================
// DELETE EMPLOYEE
// DELETE /api/employees/{id}
// =============================================

function deleteEmployee(id) {
    // Ask for confirmation before deleting
    if (!confirm("Are you sure you want to delete this employee?")) {
        return;
    }

    fetch(API_URL + "/" + id, {
        method: "DELETE"
    })
        .then(function (response) {
            if (response.ok) {
                showAlert("Employee deleted successfully.", "success");
                loadAllEmployees(); // Refresh the table
            } else {
                return response.json().then(function (err) {
                    throw err;
                });
            }
        })
        .catch(function (error) {
            showAlert(error.message || "Could not delete employee.", "error");
        });
}

// =============================================
// HELPER: Get form data as an object
// =============================================

function getFormData() {
    return {
        name: document.getElementById("name").value.trim(),
        email: document.getElementById("email").value.trim(),
        department: document.getElementById("department").value.trim(),
        position: document.getElementById("position").value.trim(),
        salary: parseFloat(document.getElementById("salary").value)
    };
}

// =============================================
// HELPER: Basic client-side form validation
// =============================================

function validateForm(employee) {
    const errorDiv = document.getElementById("form-error");

    const errors = [];

    if (!employee.name) errors.push("Name is required.");
    if (!employee.email) errors.push("Email is required.");
    if (!employee.department) errors.push("Department is required.");
    if (!employee.position) errors.push("Position is required.");
    if (!employee.salary || employee.salary <= 0) errors.push("Salary must be greater than 0.");

    if (errors.length > 0) {
        errorDiv.style.display = "block";
        errorDiv.innerHTML = errors.join("<br>");
        return false;
    }

    errorDiv.style.display = "none";
    return true;
}

// =============================================
// HELPER: Display server-side validation errors
// =============================================

function displayServerErrors(error) {
    const errorDiv = document.getElementById("form-error");

    if (error.fieldErrors) {
        // Server returned field-level validation errors
        const messages = Object.values(error.fieldErrors);
        errorDiv.style.display = "block";
        errorDiv.innerHTML = messages.join("<br>");
    } else if (error.message) {
        errorDiv.style.display = "block";
        errorDiv.innerHTML = error.message;
    } else {
        errorDiv.style.display = "block";
        errorDiv.innerHTML = "An unexpected error occurred.";
    }
}

// =============================================
// HELPER: Show success or error alert at top of table
// =============================================

function showAlert(message, type) {
    const alertDiv = document.getElementById("alert");
    alertDiv.textContent = message;
    alertDiv.className = "alert " + type; // "alert success" or "alert error"
    alertDiv.style.display = "block";

    // Auto-hide after 4 seconds
    setTimeout(function () {
        alertDiv.style.display = "none";
    }, 4000);
}

// =============================================
// HELPER: Reset form to "Add" mode
// =============================================

function clearForm() {
    document.getElementById("employee-id").value = "";
    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("department").value = "";
    document.getElementById("position").value = "";
    document.getElementById("salary").value = "";
    document.getElementById("form-error").style.display = "none";
}

function cancelEdit() {
    clearForm();
    document.getElementById("form-title").textContent = "Add New Employee";
    document.getElementById("submit-btn").textContent = "Add Employee";
    document.getElementById("submit-btn").style.backgroundColor = "";
    document.getElementById("cancel-btn").style.display = "none";
}
