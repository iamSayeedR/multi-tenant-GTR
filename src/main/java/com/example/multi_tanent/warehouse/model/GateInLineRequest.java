package com.example.multi_tanent.warehouse.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GateInLineRequest {
    @NotNull(message = "Item ID is required")
    private Long itemId;

    private String rfidTagCode;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long qty;

    // Only toLocationId is needed for Gate In (inbound from suppliers)
    private Long toLocationId;
}
