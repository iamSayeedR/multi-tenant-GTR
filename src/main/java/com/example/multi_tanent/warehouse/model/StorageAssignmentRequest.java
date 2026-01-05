package com.example.multi_tanent.warehouse.model;

import lombok.Data;

@Data
public class StorageAssignmentRequest {
    private Long gateInLineId;
    private Long assignedToId; // Optional: StoreKeeper ID. If null, auto-assigns available storekeeper on
                               // target floor
    private Long targetLocationId; // Can be warehouse/zone/floor/rack - will auto-select BIN
    private Long quantityToStore;
}
