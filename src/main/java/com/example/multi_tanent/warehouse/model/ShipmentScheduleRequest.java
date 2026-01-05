package com.example.multi_tanent.warehouse.model;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class ShipmentScheduleRequest {
    private LocalDate expectedArrivalDate;
    private String driverName;
    private String truckNumber;
    private String supplierName;
    private Long warehouseId;
    private String remarks;
    private List<ShipmentScheduleLineRequest> lines;

    @Data
    public static class ShipmentScheduleLineRequest {
        private Long itemId;
        private Long expectedQuantity;
    }
}
