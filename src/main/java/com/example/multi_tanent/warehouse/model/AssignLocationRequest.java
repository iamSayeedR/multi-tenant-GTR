package com.example.multi_tanent.warehouse.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignLocationRequest {
    @NotNull(message = "Line ID is required")
    private Long lineId;

    @NotNull(message = "To location ID is required")
    private Long toLocationId;
}
