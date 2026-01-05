package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.entity.GateInStatus;
import com.example.multi_tanent.warehouse.model.*;
import com.example.multi_tanent.warehouse.service.GateInService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gate-in")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
@Tag(name = "Gate In", description = "Inbound workflow - Receive goods at warehouse gate")
public class GateInController {

    private final GateInService gateInService;

//   // public GateInController(GateInService gateInService) {
//        this.gateInService = gateInService;
//    }

    @PostMapping
    @Operation(summary = "Create Gate In", description = "Receive goods at warehouse gate with RFID tags")
    public ResponseEntity<GateInResponse> createGateIn(@Validated @RequestBody GateInRequest req) {
        return ResponseEntity.ok(gateInService.createGateIn(req));
    }

    @GetMapping
    @Operation(summary = "List Gate Ins", description = "Get all gate in records, optionally filtered by status")
    public ResponseEntity<List<GateInResponse>> listGateIns(
            @RequestParam(required = false) List<GateInStatus> status) {
        return ResponseEntity.ok(gateInService.listGateIns(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GateInResponse> getGateIn(@PathVariable Long id) {
        return ResponseEntity.ok(gateInService.getGateInById(id));
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<GateInResponse> confirmGateIn(@PathVariable Long id) {
        return ResponseEntity.ok(gateInService.confirmGateIn(id));
    }

    @PostMapping("/{id}/assign-location")
    public ResponseEntity<GateInResponse> assignLocation(
            @PathVariable Long id,
            @Validated @RequestBody AssignLocationRequest req) {
        return ResponseEntity.ok(gateInService.assignLocation(id, req));
    }

    @PostMapping("/{id}/attach-rfid")
    public ResponseEntity<GateInResponse> attachRFIDTags(
            @PathVariable Long id,
            @Validated @RequestBody AttachRFIDTagsRequest req) {
        return ResponseEntity.ok(gateInService.attachRFIDTags(id, req.getRfidTags()));
    }
}
