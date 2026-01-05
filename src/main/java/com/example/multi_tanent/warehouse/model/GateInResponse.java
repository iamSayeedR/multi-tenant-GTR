package com.example.multi_tanent.warehouse.model;

import com.example.multi_tanent.warehouse.entity.GateInStatus;
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
public class GateInResponse {
    private Long id;
    private String gateInNo;
    private LocalDateTime timestamp;
    private String vehicleNo;
    private Long gateKeeperId;
    private String gateKeeperName;
    private Long storeKeeperId;
    private String storeKeeperName;
    private String remarks;
    private GateInStatus status;
    private List<GateInLineResponse> lines;

    // Inter-warehouse transfer
    private Long sourceWarehouseId;
    private String sourceWarehouseName;

    // Supplier purchase
    private String supplierName;
    private String supplierAddress;

    // Link to Gate Out
    private Long referenceGateOutId;
    private String referenceGateOutNo;
}
