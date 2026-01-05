package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.ShipmentScheduleRequest;
import com.example.multi_tanent.warehouse.model.ShipmentScheduleResponse;
import com.example.multi_tanent.warehouse.service.ShipmentScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whShipmentScheduleController")
@RequestMapping("/api/shipments")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Shipment Schedule", description = "Schedule and track inbound shipments")
public class ShipmentScheduleController {

    private final ShipmentScheduleService shipmentScheduleService;

    public ShipmentScheduleController(ShipmentScheduleService shipmentScheduleService) {
        this.shipmentScheduleService = shipmentScheduleService;
    }

    @PostMapping
    @Operation(summary = "Create shipment schedule", description = "Schedule a new inbound shipment")
    public ResponseEntity<ShipmentScheduleResponse> createSchedule(@RequestBody ShipmentScheduleRequest request) {
        return ResponseEntity.ok(shipmentScheduleService.createSchedule(request));
    }

    @GetMapping
    @Operation(summary = "List shipment schedules", description = "Get all shipment schedules, optionally filtered by status")
    public ResponseEntity<List<ShipmentScheduleResponse>> listSchedules(
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status) {
        return ResponseEntity.ok(shipmentScheduleService.listSchedules(status));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get shipment by ID", description = "Retrieve a specific shipment schedule")
    public ResponseEntity<ShipmentScheduleResponse> getScheduleById(
            @Parameter(description = "Shipment ID") @PathVariable Long id) {
        return ResponseEntity.ok(shipmentScheduleService.getScheduleById(id));
    }

    @PostMapping("/{id}/link-gate-in/{gateInId}")
    @Operation(summary = "Link gate in to shipment", description = "Associate a gate in record with a shipment schedule")
    public ResponseEntity<ShipmentScheduleResponse> linkGateIn(
            @Parameter(description = "Shipment ID") @PathVariable Long id,
            @Parameter(description = "Gate In ID") @PathVariable Long gateInId) {
        return ResponseEntity.ok(shipmentScheduleService.linkGateIn(id, gateInId));
    }
}
