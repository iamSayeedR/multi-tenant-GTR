package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.entity.GateOutStatus;
import com.example.multi_tanent.warehouse.model.*;
import com.example.multi_tanent.warehouse.service.GateOutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("whGateOutController")
@RequestMapping("/api/gate-out")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Gate Out", description = "Outbound workflow - Ship goods from warehouse")
public class GateOutController {

    private final GateOutService gateOutService;

    public GateOutController(GateOutService gateOutService) {
        this.gateOutService = gateOutService;
    }

    @PostMapping
    @Operation(summary = "Create Gate Out", description = "Create outbound shipment record")
    public ResponseEntity<GateOutResponse> createGateOut(@Validated @RequestBody GateOutRequest req) {
        return ResponseEntity.ok(gateOutService.createGateOut(req));
    }

    @GetMapping
    @Operation(summary = "List Gate Outs", description = "Get all gate out records, optionally filtered by status")
    public ResponseEntity<List<GateOutResponse>> listGateOuts(
            @Parameter(description = "Filter by status") @RequestParam(required = false) List<GateOutStatus> status) {
        return ResponseEntity.ok(gateOutService.listGateOuts(status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Gate Out by ID", description = "Retrieve specific gate out record")
    public ResponseEntity<GateOutResponse> getGateOut(
            @Parameter(description = "Gate Out ID") @PathVariable Long id) {
        return ResponseEntity.ok(gateOutService.getGateOutById(id));
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm Gate Out", description = "Confirm shipment has left the warehouse")
    public ResponseEntity<GateOutResponse> confirmGateOut(
            @Parameter(description = "Gate Out ID") @PathVariable Long id) {
        return ResponseEntity.ok(gateOutService.confirmGateOut(id));
    }

    @PostMapping("/{id}/attach-rfid")
    @Operation(summary = "Attach RFID tags", description = "Associate RFID tags with outbound shipment")
    public ResponseEntity<GateOutResponse> attachRFIDTags(
            @Parameter(description = "Gate Out ID") @PathVariable Long id,
            @Validated @RequestBody AttachRFIDTagsRequest req) {
        return ResponseEntity.ok(gateOutService.attachRFIDTags(id, req.getRfidTags()));
    }
}
