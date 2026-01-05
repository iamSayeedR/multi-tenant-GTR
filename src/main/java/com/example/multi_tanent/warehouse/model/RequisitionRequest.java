package com.example.multi_tanent.warehouse.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RequisitionRequest {
    private Long entityId;
    private Long recipientId;
    private Long requestedById;
    private LocalDate requiredDate;
    private LocalDate fromDate;
    private Long customerOrderId;
    private String basisType;
    private Long basisId;
    private Long projectId;
    private Long projectTaskId;
    private String comment;
    private List<RequisitionLineRequest> lines = new ArrayList<>();
}
