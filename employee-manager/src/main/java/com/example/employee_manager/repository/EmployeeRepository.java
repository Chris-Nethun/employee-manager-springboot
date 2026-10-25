package com.example.employee_manager.repository;

import com.example.employee_manager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    // Check for duplicate emails during registration
    boolean existsByEmail(String email);
    
    // Check for duplicate emails during updates
    boolean existsByEmailAndIdNot(String email, Long id);

    // Task 11 (Challenge): Search employees by name (case-insensitive)
    List<Employee> findByFullNameContainingIgnoreCase(String keyword);
}