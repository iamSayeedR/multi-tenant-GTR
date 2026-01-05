package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.Inventory;
import com.example.multi_tanent.warehouse.model.InventoryResponse;
import com.example.multi_tanent.warehouse.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whWarehouseInventoryController")
@RequestMapping("/api/warehouse-inventory")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Warehouse Inventory", description = "Manage warehouse inventory tracking")
public class WarehouseInventoryController {

    public final InventoryService inventoryService;

    public WarehouseInventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    @Operation(summary = "Upload inventory", description = "Add or update inventory stock levels")
    public ResponseEntity<String> uploadInventory(@RequestBody Inventory inventory) {
        inventoryService.uploadInventory(inventory);
        return ResponseEntity.ok("Inventory uploaded");
    }

    @GetMapping
    @Operation(summary = "List all inventory", description = "Get all inventory records with current stock levels")
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        List<InventoryResponse> inventories = inventoryService.getAllInventory();
        return ResponseEntity.ok(inventories);
    }
}
