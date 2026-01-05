package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.GateKeeperRequest;
import com.example.multi_tanent.warehouse.model.GateKeeperResponse;
import com.example.multi_tanent.warehouse.service.GateKeeperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController("whGateKeeperController")
@RequestMapping("/api/gate-keepers")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Gate Keeper Management", description = "Manage gate keepers and their assignments")
public class GateKeeperController {

    private final GateKeeperService service;

    public GateKeeperController(GateKeeperService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create gate keeper", description = "Register a new gate keeper")
    public GateKeeperResponse create(@RequestBody GateKeeperRequest req) {
        return service.create(req);
    }

    @GetMapping
    @Operation(summary = "List all gate keepers", description = "Get all gate keepers in the system")
    public List<GateKeeperResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{warehouseId}")
    @Operation(summary = "Get gate keepers by warehouse", description = "Get all gate keepers assigned to a warehouse")
    public List<GateKeeperResponse> list(
            @Parameter(description = "Warehouse ID") @PathVariable Long warehouseId) {
        return service.list(warehouseId);
    }
}
