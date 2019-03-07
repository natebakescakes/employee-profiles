package com.example.employeeprofiles.controller;

import com.example.employeeprofiles.exception.ResourceNotFoundException;
import com.example.employeeprofiles.model.Employee;
import com.example.employeeprofiles.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/employee")
    public Page<Employee> getEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    @PostMapping("/employee")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        return employeeRepository.save(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public Employee updateEmployee(@PathVariable Long employeeId, @Valid @RequestBody Employee employeeRequest) {
        return employeeRepository.findById(employeeId).map(employee -> {
            // map request to employee object with same Id.
            employee.setFirstName(employeeRequest.getFirstName());
            employee.setLastName(employeeRequest.getLastName());
            employee.setAddress(employeeRequest.getAddress());
            employee.setEmail(employeeRequest.getEmail());
            employee.setPhone(employeeRequest.getPhone());

            return employeeRepository.save(employee);
        }).orElseThrow(() -> new ResourceNotFoundException(String.format("Employee not found with id %d", employeeId)));
    }

    @DeleteMapping("/employee/{employeeId}")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long employeeId) {
        return employeeRepository.findById(employeeId)
                .map(employee -> {
                    employeeRepository.delete(employee);
                    return ResponseEntity.ok().build();
                }).orElseThrow(() -> new ResourceNotFoundException(String.format("Employee not found with id %d", employeeId)));
    }
}
