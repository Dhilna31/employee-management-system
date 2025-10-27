package com.example.employee_management.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private LocalDate createdDate;

    @OneToOne
    @JoinColumn(name = "department_head_id")
    private Employee departmentHead;

    @OneToMany(mappedBy = "department")
    @JsonIgnore // hide employees by default
    private List<Employee> employees;

    // Transient field for JSON input/output
    @Transient
    private Long departmentHeadId;

    // JSON setter for departmentHeadId
    @JsonSetter("departmentHeadId")
    public void setDepartmentHeadId(Long departmentHeadId) {
        this.departmentHeadId = departmentHeadId;
    }

    // JSON getter for departmentHeadId
    @JsonProperty("departmentHeadId")
    @Transient
    public Long getDepartmentHeadId() {
        return departmentHead != null ? departmentHead.getId() : departmentHeadId;
    }

    // Transient getter for employees when needed in JSON
    @Transient
    @JsonProperty("employees")
    public List<Employee> getEmployeesForJson() {
        return employees;
    }

    // Constructors
    public Department() {}
    public Department(String name, LocalDate createdDate) {
        this.name = name;
        this.createdDate = createdDate;
    }

    // Regular getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDate createdDate) { this.createdDate = createdDate; }

    public Employee getDepartmentHead() { return departmentHead; }
    public void setDepartmentHead(Employee departmentHead) { this.departmentHead = departmentHead; }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}
