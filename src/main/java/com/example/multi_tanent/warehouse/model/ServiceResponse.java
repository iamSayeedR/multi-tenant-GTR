package com.example.multi_tanent.warehouse.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String defaultUom;
    private String category;
    private Boolean active;
}
