package com.example.employee_management.Service;

import com.example.employee_management.Entity.Department;
import com.example.employee_management.Entity.Employee;
import com.example.employee_management.Repository.DepartmentRepository;
import com.example.employee_management.Repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
        private final EmployeeRepository employeeRepository;
        private final DepartmentRepository departmentRepository;

        public EmployeeService(EmployeeRepository employeeRepository,
                               DepartmentRepository departmentRepository) {
            this.employeeRepository = employeeRepository;
            this.departmentRepository = departmentRepository;
        }

        // Add a new employee
        public Employee addEmployee(Employee employee) {

            // Set Department from departmentId
            if (employee.getDepartmentId() != null) {
                Department dept = departmentRepository.findById(employee.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                employee.setDepartment(dept);
            }

            // Set Reporting Manager from reportingManagerId
            if (employee.getReportingManagerId() != null) {
                Employee manager = employeeRepository.findById(employee.getReportingManagerId())
                        .orElseThrow(() -> new RuntimeException("Reporting Manager not found"));
                employee.setReportingManager(manager);
            }

            return employeeRepository.save(employee);
        }

        // Get all employees
        public List<Employee> getAllEmployees() {
            return employeeRepository.findAll();
        }

        // Update an employee
        public Employee updateEmployee(Long id, Employee updatedEmployee) {
            Employee existing = employeeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            existing.setName(updatedEmployee.getName());
            existing.setSalary(updatedEmployee.getSalary());
            existing.setRole(updatedEmployee.getRole());
            existing.setAddress(updatedEmployee.getAddress());
            existing.setDob(updatedEmployee.getDob());
            existing.setJoiningDate(updatedEmployee.getJoiningDate());
            existing.setYearlyBonusPercentage(updatedEmployee.getYearlyBonusPercentage());

            // Update Department
            if (updatedEmployee.getDepartmentId() != null) {
                Department dept = departmentRepository.findById(updatedEmployee.getDepartmentId())
                        .orElseThrow(() -> new RuntimeException("Department not found"));
                existing.setDepartment(dept);
            } else {
                // explicitly set department to null if departmentId is null
                existing.setDepartment(null);
            }

            // Update Reporting Manager
            if (updatedEmployee.getReportingManagerId() != null) {
                Employee manager = employeeRepository.findById(updatedEmployee.getReportingManagerId())
                        .orElseThrow(() -> new RuntimeException("Reporting Manager not found"));
                existing.setReportingManager(manager);
            } else {
                // explicitly set reporting manager to null if reportingManagerId is null
                existing.setReportingManager(null);
            }

            return employeeRepository.save(existing);
        }

        // Delete an employee
        public void deleteEmployee(Long id) {
            employeeRepository.deleteById(id);
        }

    // Move employee to another department
    public Employee moveEmployeeToDepartment(Long employeeId, Long departmentId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Department newDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        employee.setDepartment(newDepartment);

        return employeeRepository.save(employee);
    }

    // Paginated employees
    public Page<Employee> getPaginatedEmployees(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return employeeRepository.findAll(pageable);
    }

    // Lookup: return only id and name
    public List<Map<String, Object>> getEmployeeIdAndName() {
        return employeeRepository.findAll()
                .stream()
                .map(emp -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", emp.getId());
                    map.put("name", emp.getName());
                    return map;
                })
                .collect(Collectors.toList());
    }

}


