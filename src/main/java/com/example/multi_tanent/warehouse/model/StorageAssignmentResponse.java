package com.example.multi_tanent.warehouse.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageAssignmentResponse {
    private Long id;
    private String assignmentNo;
    private Long gateInLineId;
    private Long itemId;
    private String itemName;
    private Long assignedToId;
    private String assignedToName;
    private String assignedToCode;
    private Long targetLocationId;
    private String binName;
    private String rackName;
    private String floorName;
    private String zoneName;
    private Long quantityToStore;
    private String status;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
    private String completionRemarks;
}
