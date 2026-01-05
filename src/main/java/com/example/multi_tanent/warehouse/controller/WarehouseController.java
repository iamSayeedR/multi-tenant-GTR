package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.WarehouseRequest;
import com.example.multi_tanent.warehouse.model.WarehouseResponse;
import com.example.multi_tanent.warehouse.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController("whWarehouseController")
@RequestMapping("/api/warehouses")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Warehouse Master", description = "Manage warehouse master data")
public class WarehouseController {

    private final WarehouseService service;

    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create warehouse", description = "Add a new warehouse to the system")
    public WarehouseResponse create(@RequestBody WarehouseRequest request) {
        return service.create(request);
    }

    @GetMapping
    @Operation(summary = "List all warehouses", description = "Get all warehouses in the system")
    public List<WarehouseResponse> getAll() {
        return service.getAll();
    }
}
