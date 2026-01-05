package com.example.multi_tanent.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseInfo {
    private Long id;
    private String name;
    private String address;
    private Boolean active;
}
