package com.example.multi_tanent.warehouse.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private BigDecimal budgetAmount;
    private Boolean active;
}
