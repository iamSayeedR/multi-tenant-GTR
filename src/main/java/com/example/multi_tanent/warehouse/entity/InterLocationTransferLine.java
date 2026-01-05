package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inter_location_transfer_line")
@Getter
@Setter
@NoArgsConstructor
public class InterLocationTransferLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "transfer_id", nullable = false)
    private InterLocationTransfer transfer;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity itemEntity;
    @Column(name = "requisition_qty", nullable = false)
    private Long requisitionQty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterLocationTransfer getTransfer() {
        return transfer;
    }

    public void setTransfer(InterLocationTransfer transfer) {
        this.transfer = transfer;
    }

    public ItemEntity getItem() {
        return itemEntity;
    }

    public void setItem(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }

    public Long getRequisitionQty() {
        return requisitionQty;
    }

    public void setRequisitionQty(Long requisitionQty) {
        this.requisitionQty = requisitionQty;
    }

    public Long getIssuedQty() {
        return issuedQty;
    }

    public void setIssuedQty(Long issuedQty) {
        this.issuedQty = issuedQty;
    }

    public BigDecimal getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(BigDecimal ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    public BigDecimal getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(BigDecimal vatPercent) {
        this.vatPercent = vatPercent;
    }

    @Column(name = "issued_qty", nullable = false)
    private Long issuedQty = 0L;
    @Column(name = "rate_per_unit")
    private BigDecimal ratePerUnit;
    @Column(name = "vat_percent")
    private BigDecimal vatPercent;
}
