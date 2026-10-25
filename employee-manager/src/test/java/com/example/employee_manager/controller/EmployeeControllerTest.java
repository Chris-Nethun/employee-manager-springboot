package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeControllerTest {

    @Test
    void saveEmployeeRejectsAnAlreadyRegisteredEmail() {
        EmployeeService employeeService = mock(EmployeeService.class);
        when(employeeService.isEmailTaken("existing@example.com")).thenReturn(true);
        when(employeeService.getAllEmployees(null)).thenReturn(List.of());
        EmployeeController controller = new EmployeeController(employeeService);

        Employee employee = new Employee();
        employee.setFullName("Test Employee");
        employee.setEmail("existing@example.com");
        employee.setPhone("555-0100");
        employee.setDepartment("Engineering");
        employee.setSalary(50000.0);
        BeanPropertyBindingResult result = new BeanPropertyBindingResult(employee, "employee");

        String view = controller.saveEmployee(
                employee, result, new ExtendedModelMap(), new RedirectAttributesModelMap());

        assertEquals("employees", view);
        assertTrue(result.hasFieldErrors("email"));
        assertEquals("This email is already registered",
                result.getFieldError("email").getDefaultMessage());
        verify(employeeService, never()).saveEmployee(employee);
    }
}
