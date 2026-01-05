package com.example.multi_tanent.warehouse.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GateOutRequest {
    @NotBlank(message = "Vehicle number is required")
    private String vehicleNo;

    @NotNull(message = "Gate keeper ID is required")
    private Long gateKeeperId;

    private Long storeKeeperId;
    private String remarks;
    private List<GateOutLineRequest> lines;

    // Inter-warehouse transfer
    private Long destinationWarehouseId;

    // Customer delivery
    private String customerName;
    private String customerAddress;

    // Link to Gate In (for tracking)
    private Long referenceGateInId;
}
