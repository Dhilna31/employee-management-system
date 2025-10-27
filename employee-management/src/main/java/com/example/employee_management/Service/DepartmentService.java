package com.example.employee_management.Service;

import com.example.employee_management.Entity.Department;
import com.example.employee_management.Entity.Employee;
import com.example.employee_management.Repository.DepartmentRepository;
import com.example.employee_management.Repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    public DepartmentService(DepartmentRepository departmentRepository,
                             EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    // Add a new department
    public Department addDepartment(Department department) {
        if (department.getDepartmentHeadId() != null) {
            Employee head = employeeRepository.findById(department.getDepartmentHeadId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Department head not found"));
            department.setDepartmentHead(head);
        }
        return departmentRepository.save(department);
    }

    // Update a department
    public Department updateDepartment(Long id, Department updatedDepartment) {
        Department existing = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Department not found"));

        existing.setName(updatedDepartment.getName());

        if (updatedDepartment.getDepartmentHeadId() != null) {
            Employee head = employeeRepository.findById(updatedDepartment.getDepartmentHeadId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Department head not found"));
            existing.setDepartmentHead(head);
        } else {
            existing.setDepartmentHead(null); // allow removal of department head
        }

        return departmentRepository.save(existing);
    }

    // Get all departments
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Delete a department
    public void deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Department not found"));

        if (department.getEmployees() != null && !department.getEmployees().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cannot delete department. Employees are assigned to it.");
        }

        departmentRepository.deleteById(id);
    }
}
