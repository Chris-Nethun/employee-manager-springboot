package com.example.employee_manager.controller;

import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // READ: Show all employees and the form
    @GetMapping
    public String listEmployees(@RequestParam(required = false) String keyword, Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees(keyword));
        model.addAttribute("keyword", keyword);
        if (!model.containsAttribute("employee")) {
            model.addAttribute("employee", new Employee());
        }
        return "employees";
    }

    // CREATE: Save a new employee
    @PostMapping
    public String saveEmployee(@Valid @ModelAttribute("employee") Employee employee,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (!result.hasFieldErrors("email") && employeeService.isEmailTaken(employee.getEmail())) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }

        // Return to page if validation errors exist
        if (result.hasErrors()) {
            model.addAttribute("employees", employeeService.getAllEmployees(null));
            return "employees";
        }

        employeeService.saveEmployee(employee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee saved successfully!");
        return "redirect:/employees"; // Post-Redirect-Get pattern
    }

    // EDIT: Show the edit form
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Employee employee = employeeService.getEmployeeById(id).orElse(null);
        if (employee == null) {
            return "redirect:/employees";
        }
        model.addAttribute("employee", employee);
        return "edit-employee";
    }

    // UPDATE: Save changes to existing employee
    @PostMapping("/{id}/update")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") Employee employee,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes) {
        if (!result.hasFieldErrors("email")
                && employeeService.isEmailTakenByAnother(employee.getEmail(), id)) {
            result.rejectValue("email", "duplicate", "This email is already registered");
        }

        if (result.hasErrors()) {
            return "edit-employee";
        }

        Employee existingEmployee = employeeService.getEmployeeById(id).orElse(null);
        if (existingEmployee == null) {
            return "redirect:/employees";
        }
        BeanUtils.copyProperties(employee, existingEmployee, "id");
        employeeService.saveEmployee(existingEmployee);
        redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
        return "redirect:/employees";
    }

    // DELETE: Remove employee
    @PostMapping("/{id}/delete")
    public String deleteEmployee(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        employeeService.deleteEmployeeById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        return "redirect:/employees";
    }
}