package com.example.multi_tanent.warehouse.model;

import lombok.Builder;

@Builder
public record DepartmentRequest(String name, String code, String type, Long warehouseId) {
}
