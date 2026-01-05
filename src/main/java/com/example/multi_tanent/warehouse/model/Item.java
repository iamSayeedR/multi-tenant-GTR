package com.example.multi_tanent.warehouse.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String category;
    private String uom;
    private BigDecimal defaultRate;
    private BigDecimal defaultVatPercent;
}

