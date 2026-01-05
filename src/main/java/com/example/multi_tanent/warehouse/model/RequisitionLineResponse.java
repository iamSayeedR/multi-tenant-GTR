package com.example.multi_tanent.warehouse.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class RequisitionLineResponse {
    private Long id;
    private Integer lineNumber;
    private Long itemId;
    private String itemName;
    private Long serviceId;
    private String serviceName;
    private Long expenseItemId;
    private String expenseItemName;
    private String content;
    private BigDecimal quantityTotal;
    private BigDecimal quantityToPurchase;
    private BigDecimal quantityToTransfer;
    private String uom;
    private BigDecimal stockBalance;
    private BigDecimal remainingBudgetQty;
    private Long projectTaskId;
    private String projectTaskName;
    private String transactionContent;
    private String fulfillmentMethod;
    private Long fulfillmentSourceId;
    private BigDecimal price;
    private Boolean pricesIncludeVat;
    private BigDecimal vatPercentage;
    private BigDecimal amountExclVat;
    private BigDecimal vatAmount;
    private BigDecimal totalAmount;
}
