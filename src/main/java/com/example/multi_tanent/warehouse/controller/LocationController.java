package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.Location;
import com.example.multi_tanent.warehouse.model.LocationNode;
import com.example.multi_tanent.warehouse.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whLocationController")
@RequestMapping("/api/warehouse-locations")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Location Management", description = "Manage warehouse locations and hierarchies")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    @Operation(summary = "Create location", description = "Add a new storage location")
    public ResponseEntity<String> uploadLocation(@RequestBody Location location) {
        locationService.uploadLocation(location);
        return ResponseEntity.ok("Location saved");
    }

    @GetMapping
    @Operation(summary = "List all locations", description = "Get all storage locations")
    public ResponseEntity<List<Location>> getAllLocations() {
        List<Location> locations = locationService.findAll();
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/children/{parentId}")
    @Operation(summary = "Get child locations", description = "Get all child locations of a parent location")
    public List<Location> getChildren(
            @Parameter(description = "Parent location ID") @PathVariable Long parentId) {
        return locationService.getChildren(parentId);
    }

    @GetMapping("/warehouse/{warehouseId}")
    @Operation(summary = "Get locations by warehouse", description = "Get all locations in a specific warehouse")
    public List<Location> getByWarehouse(
            @Parameter(description = "Warehouse ID") @PathVariable Long warehouseId) {
        return locationService.getByWarehouse(warehouseId);
    }

    @GetMapping("/tree/{warehouseId}")
    @Operation(summary = "Get location tree", description = "Get hierarchical location tree for a warehouse")
    public List<LocationNode> getTree(
            @Parameter(description = "Warehouse ID") @PathVariable Long warehouseId) {
        return locationService.getLocationTree(warehouseId);
    }
}
