package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Data;

@Entity
@Table(name = "requisition_line")
@Data
public class RequisitionLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requisition_id", nullable = false)
    private RequisitionEntity requisition;

    @Column(nullable = false)
    private Integer lineNumber;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity service;

    @ManyToOne
    @JoinColumn(name = "expense_item_id")
    private ExpenseItemEntity expenseItem;

    @Column(length = 5000)
    private String content;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal quantityTotal;

    @Column(precision = 15, scale = 3)
    private BigDecimal quantityToPurchase = BigDecimal.ZERO;

    @Column(precision = 15, scale = 3)
    private BigDecimal quantityToTransfer = BigDecimal.ZERO;

    @Column(length = 20)
    private String uom;

    @Column(precision = 15, scale = 3)
    private BigDecimal stockBalance;

    @Column(precision = 15, scale = 3)
    private BigDecimal remainingBudgetQty;

    @ManyToOne
    @JoinColumn(name = "project_task_id")
    private ProjectTaskEntity projectTask;

    @Column(length = 5000)
    private String transactionContent;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FulfillmentMethod fulfillmentMethod;

    @Column(name = "fulfillment_source_id")
    private Long fulfillmentSourceId; // Supplier/Department/Warehouse ID

    @Column(precision = 15, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Boolean pricesIncludeVat = false;

    @Column(precision = 5, scale = 2)
    private BigDecimal vatPercentage = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal amountExclVat = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal vatAmount = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    /**
     * Calculate amounts based on price, quantity, and VAT settings
     */
    public void calculateAmounts() {
        if (price == null || quantityTotal == null) {
            return;
        }

        BigDecimal baseAmount = price.multiply(quantityTotal);

        if (pricesIncludeVat) {
            // Price includes VAT - need to extract VAT
            BigDecimal divisor = BigDecimal.ONE.add(vatPercentage.divide(BigDecimal.valueOf(100)));
            amountExclVat = baseAmount.divide(divisor, 2, RoundingMode.HALF_UP);
            vatAmount = baseAmount.subtract(amountExclVat);
            totalAmount = baseAmount;
        } else {
            // Price excludes VAT - need to add VAT
            amountExclVat = baseAmount;
            vatAmount = baseAmount.multiply(vatPercentage.divide(BigDecimal.valueOf(100)));
            totalAmount = amountExclVat.add(vatAmount);
        }
    }
}
