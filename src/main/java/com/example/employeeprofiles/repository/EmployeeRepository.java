package com.example.employeeprofiles.repository;

import com.example.employeeprofiles.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findByLastName(String lastName);
}