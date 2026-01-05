package com.example.multi_tanent.warehouse.model;

import lombok.Builder;

@Builder
public record ServiceRequest(
        String name,
        String code,
        String description,
        String defaultUom,
        String category) {
}
