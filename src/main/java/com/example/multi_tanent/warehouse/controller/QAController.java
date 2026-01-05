package com.example.multi_tanent.warehouse.controller;

import com.example.multi_tanent.warehouse.model.QARequest;
import com.example.multi_tanent.warehouse.model.QAResponse;
import com.example.multi_tanent.warehouse.service.QAService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("whQAController")
@RequestMapping("/api/qa")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(name = "Quality Assurance", description = "Quality inspection and approval workflow")
public class QAController {

    private final QAService qaService;

    public QAController(QAService qaService) {
        this.qaService = qaService;
    }

    @PostMapping
    @Operation(summary = "Perform QA", description = "Perform quality assurance check on received goods")
    public ResponseEntity<QAResponse> performQA(@RequestBody QARequest request) {
        return ResponseEntity.ok(qaService.performQA(request));
    }

    @GetMapping("/gate-in/{gateInId}")
    @Operation(summary = "Get QA by Gate In ID", description = "Retrieve QA record for a specific gate in")
    public ResponseEntity<QAResponse> getQAByGateInId(
            @Parameter(description = "Gate In ID") @PathVariable Long gateInId) {
        return ResponseEntity.ok(qaService.getQAByGateInId(gateInId));
    }

    @GetMapping
    @Operation(summary = "List all QA records", description = "Get all quality assurance records")
    public ResponseEntity<List<QAResponse>> getAllQARecords() {
        return ResponseEntity.ok(qaService.getAllQARecords());
    }
}
