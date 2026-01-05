package com.example.multi_tanent.warehouse.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ProjectRequest(
        String name,
        String code,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        String status,
        BigDecimal budgetAmount) {
}
