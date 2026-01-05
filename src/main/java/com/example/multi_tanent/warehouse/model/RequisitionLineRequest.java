package com.example.multi_tanent.warehouse.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class RequisitionLineRequest {
    private Long itemId;
    private Long serviceId;
    private Long expenseItemId;
    private String content;
    private BigDecimal quantityTotal;
    private BigDecimal quantityToPurchase;
    private BigDecimal quantityToTransfer;
    private String uom;
    private Long projectTaskId;
    private String transactionContent;
    private String fulfillmentMethod; // PURCHASE, PRODUCTION, TRANSFER
    private Long fulfillmentSourceId;
    private BigDecimal price;
    private Boolean pricesIncludeVat;
    private BigDecimal vatPercentage;
}
