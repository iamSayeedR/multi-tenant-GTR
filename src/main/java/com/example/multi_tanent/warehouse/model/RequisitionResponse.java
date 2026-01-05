package com.example.multi_tanent.warehouse.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RequisitionResponse {
    private Long id;
    private String requisitionNo;
    private Long entityId;
    private String entityName;
    private Long recipientId;
    private String recipientName;
    private Long requestedById;
    private String requestedByName;
    private LocalDate requiredDate;
    private String status;
    private LocalDate fromDate;
    private Long customerOrderId;
    private String basisType;
    private Long basisId;
    private Long projectId;
    private String projectName;
    private Long projectTaskId;
    private String projectTaskName;
    private String comment;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RequisitionLineResponse> lines = new ArrayList<>();
}
