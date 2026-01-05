package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.EmployeeRequest;
import com.example.multi_tanent.warehouse.model.EmployeeResponse;
import com.example.multi_tanent.warehouse.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whEmployeeController")
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Employee Management", description = "Manage employees and requesters")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    @Operation(summary = "Create employee", description = "Create a new employee record")
    public ResponseEntity<EmployeeResponse> create(@RequestBody EmployeeRequest request) {
        EmployeeResponse response = employeeService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all employees", description = "Get all active employees")
    public ResponseEntity<List<EmployeeResponse>> listAll() {
        List<EmployeeResponse> employees = employeeService.listAll();
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieve a specific employee")
    public ResponseEntity<EmployeeResponse> getById(
            @Parameter(description = "Employee ID") @PathVariable Long id) {
        EmployeeResponse employee = employeeService.getById(id);
        return ResponseEntity.ok(employee);
    }
}
