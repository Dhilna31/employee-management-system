package com.example.employee_management.Controller;

import com.example.employee_management.Entity.Department;
import com.example.employee_management.Service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    // Create a department
    @PostMapping
    public Department create(@RequestBody Department department) {
        return departmentService.addDepartment(department);
    }

    // Get all departments (supports expand)
    @GetMapping
    public List<Department> getAll(@RequestParam(required = false) String expand) {
        List<Department> departments = departmentService.getAllDepartments();

        // Include employees if expand is "true" or "employee"
        if ("true".equalsIgnoreCase(expand) || "employee".equalsIgnoreCase(expand)) {
            departments.forEach(dept -> dept.getEmployees().size()); // force initialization
        } else {
            departments.forEach(dept -> dept.setEmployees(null)); // hide employees
        }

        return departments;
    }

    // Update a department
    @PutMapping("/{id}")
    public Department update(@PathVariable Long id, @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }

    // Delete a department
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
    }
}
