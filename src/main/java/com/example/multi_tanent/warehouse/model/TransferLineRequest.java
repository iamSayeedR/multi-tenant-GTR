package com.example.multi_tanent.warehouse.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferLineRequest {
    @NotNull
    private Long itemId;
    @NotNull
    @Positive
    private Long requisitionQty;

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getRequisitionQty() {
        return requisitionQty;
    }

    public void setRequisitionQty(Long requisitionQty) {
        this.requisitionQty = requisitionQty;
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

    private BigDecimal ratePerUnit;
    private BigDecimal vatPercent;
}
