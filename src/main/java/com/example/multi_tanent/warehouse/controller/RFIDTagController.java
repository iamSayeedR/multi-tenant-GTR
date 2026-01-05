package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.RFIDTagRequest;
import com.example.multi_tanent.warehouse.model.RFIDTagResponse;
import com.example.multi_tanent.warehouse.service.RFIDTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController("whRFIDTagController")
@RequestMapping("/api/rfid-tags")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "RFID Tag Management", description = "Register and manage RFID tags")
public class RFIDTagController {

    private final RFIDTagService service;

    public RFIDTagController(RFIDTagService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Register RFID tag", description = "Register a new RFID tag in the system")
    public RFIDTagResponse register(@RequestBody RFIDTagRequest req) {
        return service.register(req);
    }

    @GetMapping
    @Operation(summary = "List all RFID tags", description = "Get all registered RFID tags")
    public List<RFIDTagResponse> getAll() {
        return service.getAll();
    }
}
