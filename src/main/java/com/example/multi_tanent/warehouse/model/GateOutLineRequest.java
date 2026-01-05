package com.example.multi_tanent.warehouse.model;

import com.example.multi_tanent.warehouse.entity.GateOutReason;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GateOutLineRequest {
    @NotNull(message = "Item ID is required")
    private Long itemId;

    private String rfidTagCode;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Long qty;

    @NotNull(message = "From location ID is required")
    private Long fromLocationId;

    private GateOutReason reason;
}
