package com.example.employee_manager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return employeeRepository.findByFullNameContainingIgnoreCase(keyword.trim());
        }
        return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    public boolean isEmailTaken(String email) {
        return employeeRepository.existsByEmail(email);
    }

    public boolean isEmailTakenByAnother(String email, Long id) {
        return employeeRepository.existsByEmailAndIdNot(email, id);
    }
}