package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.entity.TransferStatus;
import com.example.multi_tanent.warehouse.model.*;
import com.example.multi_tanent.warehouse.service.TransferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("whTransferController")
@RequestMapping("/api/transfers")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Inter-Location Transfer", description = "Manage stock transfers between warehouses and locations")
public class TransferController {
    private final TransferService service;

    public TransferController(TransferService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create transfer", description = "Create a new inter-location transfer request")
    public ResponseEntity<TransferResponse> create(@Validated @RequestBody TransferRequest req) {
        return ResponseEntity.ok(service.createTransfer(req));
    }

    @GetMapping
    @Operation(summary = "List transfers", description = "Get all transfers, optionally filtered by status")
    public ResponseEntity<List<TransferResponse>> list(
            @Parameter(description = "Filter by status") @RequestParam(required = false) List<TransferStatus> status) {
        return ResponseEntity.ok(service.listTransfers(status));
    }

    @PostMapping("/{transferId}/lines/{lineId}/issue")
    @Operation(summary = "Issue transfer line", description = "Issue items for a specific transfer line")
    public ResponseEntity<IssueRecordResponse> issue(
            @Parameter(description = "Transfer ID") @PathVariable Long transferId,
            @Parameter(description = "Line ID") @PathVariable Long lineId,
            @Validated @RequestBody IssueRequest req) {
        return ResponseEntity.ok(service.issue(transferId, lineId, req));
    }

    @GetMapping("/{transferId}/lines/{lineId}/history")
    @Operation(summary = "Get issue history", description = "Get issue history for a transfer line")
    public ResponseEntity<List<IssueRecordResponse>> history(
            @Parameter(description = "Transfer ID") @PathVariable Long transferId,
            @Parameter(description = "Line ID") @PathVariable Long lineId) {
        return ResponseEntity.ok(service.history(transferId, lineId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transfer by ID", description = "Retrieve a specific transfer with all details")
    public TransferResponse getOne(
            @Parameter(description = "Transfer ID") @PathVariable Long id) {
        return service.getTransferById(id);
    }
}
