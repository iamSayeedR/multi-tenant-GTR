package com.example.multi_tanent.warehouse.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreKeeperResponse {

    private Long id;
    private String name;
    private String employeeId; // Renamed from employeeCode
    private String contactNumber; // Renamed from phone
    private String email;
    private Long warehouseId;
    private String warehouseName;
}
