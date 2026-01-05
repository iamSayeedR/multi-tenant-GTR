package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.DepartmentRequest;
import com.example.multi_tanent.warehouse.model.DepartmentResponse;
import com.example.multi_tanent.warehouse.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whDepartmentController")
@RequestMapping("/api/departments")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Department Management", description = "Manage departments and organizational units")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping
    @Operation(summary = "Create department", description = "Create a new department or organizational unit")
    public ResponseEntity<DepartmentResponse> create(@RequestBody DepartmentRequest request) {
        DepartmentResponse response = departmentService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all departments", description = "Get all active departments")
    public ResponseEntity<List<DepartmentResponse>> listAll() {
        List<DepartmentResponse> departments = departmentService.listAll();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID", description = "Retrieve a specific department")
    public ResponseEntity<DepartmentResponse> getById(
            @Parameter(description = "Department ID") @PathVariable Long id) {
        DepartmentResponse department = departmentService.getById(id);
        return ResponseEntity.ok(department);
    }
}
