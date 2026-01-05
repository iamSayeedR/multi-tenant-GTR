package com.example.multi_tanent.warehouse.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepartmentResponse {
    private Long id;
    private String name;
    private String code;
    private String type;
    private Long warehouseId;
    private String warehouseName;
    private Boolean active;
}
