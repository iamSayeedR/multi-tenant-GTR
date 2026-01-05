package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.RequisitionRequest;
import com.example.multi_tanent.warehouse.model.RequisitionResponse;
import com.example.multi_tanent.warehouse.service.RequisitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whRequisitionController")
@RequestMapping("/api/requisitions")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Requisition Management", description = "APIs for managing requisitions including creation, approval, and tracking")
public class RequisitionController {

    private final RequisitionService requisitionService;

    public RequisitionController(RequisitionService requisitionService) {
        this.requisitionService = requisitionService;
    }

    @PostMapping
    @Operation(summary = "Create a new requisition", description = "Creates a new requisition with line items. Status will be DRAFT.")
    public ResponseEntity<RequisitionResponse> createRequisition(@RequestBody RequisitionRequest request) {
        RequisitionResponse response = requisitionService.createRequisition(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all requisitions", description = "Get all requisitions, optionally filtered by status")
    public ResponseEntity<List<RequisitionResponse>> listRequisitions(
            @Parameter(description = "Filter by status (DRAFT, WAITING_FOR_PROCESSING, APPROVED, etc.)") @RequestParam(required = false) String status) {
        List<RequisitionResponse> requisitions = requisitionService.listRequisitions(status);
        return ResponseEntity.ok(requisitions);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get requisition by ID", description = "Retrieve a specific requisition with all details")
    public ResponseEntity<RequisitionResponse> getRequisition(
            @Parameter(description = "Requisition ID") @PathVariable Long id) {
        RequisitionResponse response = requisitionService.getRequisitionById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/submit")
    @Operation(summary = "Submit requisition for approval", description = "Changes status from DRAFT to WAITING_FOR_PROCESSING")
    public ResponseEntity<RequisitionResponse> submitRequisition(
            @Parameter(description = "Requisition ID") @PathVariable Long id) {
        RequisitionResponse response = requisitionService.submitForProcessing(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve requisition", description = "Changes status from WAITING_FOR_PROCESSING to APPROVED")
    public ResponseEntity<RequisitionResponse> approveRequisition(
            @Parameter(description = "Requisition ID") @PathVariable Long id) {
        RequisitionResponse response = requisitionService.approve(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject requisition", description = "Changes status to REJECTED with a reason")
    public ResponseEntity<RequisitionResponse> rejectRequisition(
            @Parameter(description = "Requisition ID") @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        String reason = payload.get("reason");
        RequisitionResponse response = requisitionService.reject(id, reason);
        return ResponseEntity.ok(response);
    }
}
