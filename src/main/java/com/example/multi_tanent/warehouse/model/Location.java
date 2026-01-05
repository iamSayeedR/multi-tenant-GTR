package com.example.multi_tanent.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    private Long id;
    private String code;
    private String name;
    private String type;
    private Long parentId;
    private Long warehouseId;
    private String description;
    private Integer capacity;
}
