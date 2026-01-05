package com.example.multi_tanent.warehouse.model;

import lombok.Data;

@Data
public class QARequest {
    private Long gateInId;
    private Integer driverRating; // 1-5
    private Integer itemQualityRating; // 1-5
    private String qaRemarks;
    private Long inspectedById; // StoreKeeper ID
}
