package com.example.multi_tanent.warehouse.model;

import com.example.multi_tanent.warehouse.entity.GateOutStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GateOutResponse {
    private Long id;
    private String gateOutNo;
    private LocalDateTime timestamp;
    private String vehicleNo;
    private Long gateKeeperId;
    private String gateKeeperName;
    private Long storeKeeperId;
    private String storeKeeperName;
    private String remarks;
    private GateOutStatus status;
    private List<GateOutLineResponse> lines;

    // Inter-warehouse transfer
    private Long destinationWarehouseId;
    private String destinationWarehouseName;

    // Customer delivery
    private String customerName;
    private String customerAddress;

    // Link to Gate In
    private Long referenceGateInId;
    private String referenceGateInNo;
}
