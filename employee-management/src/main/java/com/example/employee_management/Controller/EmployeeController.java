package com.example.employee_management.Controller;

import com.example.employee_management.Entity.Employee;
import com.example.employee_management.Service.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create employee
    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    // Get all employees with pagination and lookup
    @GetMapping
    public Map<String, Object> getAll(
            @RequestParam(required = false) Boolean lookup,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        if (Boolean.TRUE.equals(lookup)) {
            // Return only ID and Name
            List<Map<String, Object>> employees = employeeService.getEmployeeIdAndName();
            return Map.of(
                    "content", employees,
                    "page", 0,
                    "totalPages", 1,
                    "totalItems", employees.size()
            );
        }

        // Paginated full details
        Page<Employee> employeePage = employeeService.getPaginatedEmployees(page, size);

        return Map.of(
                "content", employeePage.getContent(),
                "page", employeePage.getNumber(),
                "totalPages", employeePage.getTotalPages(),
                "totalItems", employeePage.getTotalElements()
        );
    }

    // Update employee
    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    // Delete employee
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

    // Move an employee to another department
    @PatchMapping("/{employeeId}/department/{departmentId}")
    public Employee moveEmployeeToDepartment(@PathVariable Long employeeId, @PathVariable Long departmentId) {
        return employeeService.moveEmployeeToDepartment(employeeId, departmentId);
    }
}
