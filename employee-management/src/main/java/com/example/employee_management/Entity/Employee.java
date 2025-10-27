package com.example.employee_management.Entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import java.time.LocalDate;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    private double salary;
    private String address;
    private String role;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;

    private double yearlyBonusPercentage;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne
    @JoinColumn(name = "reporting_manager_id")
    @JsonIgnore  // prevents infinite recursion
    private Employee reportingManager;

    // Transient fields for JSON input
    @Transient
    private Long departmentId;

    @Transient
    private Long reportingManagerId;

    // JSON setter for department
    @JsonSetter("department")
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @JsonIgnore
    public Long getDepartmentId() {
        return departmentId;
    }

    // JSON setter for reporting manager
    @JsonSetter("reportingManagerId")
    public void setReportingManagerId(Long reportingManagerId) {
        this.reportingManagerId = reportingManagerId;
    }

    @JsonIgnore
    public Long getReportingManagerId() {
        return reportingManagerId;
    }

    // Constructors
    public Employee() {}

    public Employee(String name, LocalDate dob, double salary, String role) {
        this.name = name;
        this.dob = dob;
        this.salary = salary;
        this.role = role;
    }

    // Regular getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public LocalDate getJoiningDate() { return joiningDate; }
    public void setJoiningDate(LocalDate joiningDate) { this.joiningDate = joiningDate; }

    public double getYearlyBonusPercentage() { return yearlyBonusPercentage; }
    public void setYearlyBonusPercentage(double yearlyBonusPercentage) {
        this.yearlyBonusPercentage = yearlyBonusPercentage;
    }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    @JsonProperty("department")
    @Transient
    public Object getDepartmentForJson() {
        if (department == null) return null;
        return new Object() {
            public Long id = department.getId();
            public String name = department.getName();
        };
    }

    public Employee getReportingManager() { return reportingManager; }
    public void setReportingManager(Employee reportingManager) { this.reportingManager = reportingManager; }

    // Transient getter for JSON output to include reportingManagerId
    @JsonProperty("reportingManagerId")
    @Transient
    public Long getReportingManagerIdForJson() {
        return reportingManager != null ? reportingManager.getId() : null;
    }
}
