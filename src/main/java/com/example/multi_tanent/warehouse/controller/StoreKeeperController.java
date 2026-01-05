package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.StoreKeeperRequest;
import com.example.multi_tanent.warehouse.model.StoreKeeperResponse;
import com.example.multi_tanent.warehouse.service.StoreKeeperService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController("whStoreKeeperController")
@RequestMapping("/api/store-keepers")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Store Keeper Management", description = "Manage store keepers and their assignments")
public class StoreKeeperController {

    private final StoreKeeperService service;

    public StoreKeeperController(StoreKeeperService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create store keeper", description = "Register a new store keeper")
    public StoreKeeperResponse create(@RequestBody StoreKeeperRequest req) {
        return service.create(req);
    }

    @GetMapping
    @Operation(summary = "List all store keepers", description = "Get all store keepers in the system")
    public List<StoreKeeperResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{warehouseId}")
    @Operation(summary = "Get store keepers by warehouse", description = "Get all store keepers assigned to a warehouse")
    public List<StoreKeeperResponse> list(
            @Parameter(description = "Warehouse ID") @PathVariable Long warehouseId) {
        return service.list(warehouseId);
    }
}
