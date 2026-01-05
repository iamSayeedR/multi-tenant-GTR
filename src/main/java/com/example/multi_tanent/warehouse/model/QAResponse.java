package com.example.multi_tanent.warehouse.model;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QAResponse {
    private Long id;
    private Long gateInId;
    private String gateInNo;
    private Integer driverRating;
    private Integer itemQualityRating;
    private String qaRemarks;
    private Long inspectedById;
    private String inspectedByName;
    private LocalDateTime inspectionDate;
}
