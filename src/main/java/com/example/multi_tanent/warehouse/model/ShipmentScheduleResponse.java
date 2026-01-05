package com.example.multi_tanent.warehouse.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipmentScheduleResponse {
    private Long id;
    private String scheduleNo;
    private LocalDate expectedArrivalDate;
    private String driverName;
    private String truckNumber;
    private String supplierName;
    private Long warehouseId;
    private String warehouseName;
    private String status;
    private String remarks;
    private LocalDateTime createdAt;
    private Long gateInId;
    private String gateInNo;
    private List<ShipmentScheduleLineResponse> lines;

    @Data
    @Builder
    public static class ShipmentScheduleLineResponse {
        private Long id;
        private Long itemId;
        private String itemName;
        private String itemCode;
        private Long expectedQuantity;
    }
}
