package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.entity.RFIDMovementLogEntity;
import com.example.multi_tanent.warehouse.model.RFIDMovementRequest;
import com.example.multi_tanent.warehouse.service.RFIDMovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController("whRFIDMovementController")
@RequestMapping("/api/rfid-movements")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "RFID Movement Tracking", description = "Track RFID tag movements and location history")
public class RFIDMovementController {

    private final RFIDMovementService service;

    public RFIDMovementController(RFIDMovementService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Log RFID movement", description = "Record RFID tag movement to a new location")
    public String logMovement(@RequestBody RFIDMovementRequest req) {
        service.logMovement(req);
        return "RFID movement processed";
    }

    @GetMapping
    @Operation(summary = "Get movement logs", description = "Get all RFID movement history")
    public List<com.example.multi_tanent.warehouse.model.RFIDMovementResponse> getAll() {
        return service.getLogs();
    }
}
