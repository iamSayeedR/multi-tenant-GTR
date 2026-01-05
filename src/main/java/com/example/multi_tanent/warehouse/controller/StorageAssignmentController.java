package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.entity.StoreKeeperEntity;
import com.example.multi_tanent.warehouse.model.CompleteStorageRequest;
import com.example.multi_tanent.warehouse.model.StorageAssignmentRequest;
import com.example.multi_tanent.warehouse.model.StorageAssignmentResponse;
import com.example.multi_tanent.warehouse.service.StorageAssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whStorageAssignmentController")
@RequestMapping("/api/storage-assignments")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Storage Assignment", description = "Assign and track storage tasks for store keepers")
public class StorageAssignmentController {

    private final StorageAssignmentService storageAssignmentService;

    public StorageAssignmentController(StorageAssignmentService storageAssignmentService) {
        this.storageAssignmentService = storageAssignmentService;
    }

    @PostMapping
    @Operation(summary = "Create storage assignment", description = "Assign storage task to a store keeper")
    public ResponseEntity<StorageAssignmentResponse> createAssignment(@RequestBody StorageAssignmentRequest request) {
        return ResponseEntity.ok(storageAssignmentService.createAssignment(request));
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete storage assignment", description = "Mark storage assignment as complete with location details")
    public ResponseEntity<StorageAssignmentResponse> completeAssignment(
            @Parameter(description = "Assignment ID") @PathVariable Long id,
            @RequestBody CompleteStorageRequest request) {
        return ResponseEntity.ok(storageAssignmentService.completeAssignment(id, request));
    }

    @GetMapping
    @Operation(summary = "List storage assignments", description = "Get all storage assignments with optional filters")
    public ResponseEntity<List<StorageAssignmentResponse>> listAssignments(
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by store keeper") @RequestParam(required = false) Long storeKeeperId) {
        return ResponseEntity.ok(storageAssignmentService.listAssignments(status, storeKeeperId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get assignment by ID", description = "Retrieve a specific storage assignment")
    public ResponseEntity<StorageAssignmentResponse> getAssignmentById(
            @Parameter(description = "Assignment ID") @PathVariable Long id) {
        return ResponseEntity.ok(storageAssignmentService.getAssignmentById(id));
    }

    @GetMapping("/available-storekeepers")
    @Operation(summary = "Get available store keepers", description = "List store keepers available for assignment")
    public ResponseEntity<List<StoreKeeperEntity>> getAvailableStorekeepers(
            @Parameter(description = "Filter by floor") @RequestParam(required = false) Long floorId) {
        return ResponseEntity.ok(storageAssignmentService.getAvailableStorekeepers(floorId));
    }
}
