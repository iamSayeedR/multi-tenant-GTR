package com.example.multi_tanent.warehouse.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreKeeperRequest {

    private String name;
    private String employeeId; // Renamed from employeeCode to match test
    private String contactNumber; // Renamed from phone to match test
    private String email; // Added to match test
    private Long warehouseId;
}
