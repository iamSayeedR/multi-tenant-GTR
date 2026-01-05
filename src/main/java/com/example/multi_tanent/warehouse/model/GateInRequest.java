package com.example.multi_tanent.warehouse.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GateInRequest {
    @NotBlank(message = "Vehicle number is required")
    private String vehicleNo;

    @NotNull(message = "Gate keeper ID is required")
    private Long gateKeeperId;

    private Long storeKeeperId;
    private String remarks;
    private List<GateInLineRequest> lines;

    // Inter-warehouse transfer
    private Long sourceWarehouseId;

    // Supplier purchase
    private String supplierName;
    private String supplierAddress;

    // Link to Gate Out (for tracking)
    private Long referenceGateOutId;
}
