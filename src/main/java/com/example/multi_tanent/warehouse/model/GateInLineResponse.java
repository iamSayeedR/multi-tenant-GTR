package com.example.multi_tanent.warehouse.model;

import lombok.Data;

@Data
public class GateInLineResponse {
    private Long id;
    private Long itemId;
    private String itemName;
    private String rfidTagCode;
    private Long qty;

    // Only toLocation is relevant for Gate In
    private Long toLocationId;
    private String toLocationName;
}
