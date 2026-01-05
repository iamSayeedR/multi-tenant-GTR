package com.example.multi_tanent.warehouse.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseRequest {

    private String name;
    private String code;
    private String address;
    private String contactPerson;
    private String contactNumber;
    private Boolean active;
    private Long tenantId;
}
