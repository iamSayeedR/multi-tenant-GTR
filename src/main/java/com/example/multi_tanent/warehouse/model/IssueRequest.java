package com.example.multi_tanent.warehouse.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueRequest {
    @NotNull
    @Positive
    private Long quantity;
    private BigDecimal ratePerUnit;
    private BigDecimal vatPercent;
    private String fromRFIDTag;
    private String toRFIDTag;

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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

    public String getFromRFIDTag() {
        return fromRFIDTag;
    }

    public void setFromRFIDTag(String fromRFIDTag) {
        this.fromRFIDTag = fromRFIDTag;
    }

    public String getToRFIDTag() {
        return toRFIDTag;
    }

    public void setToRFIDTag(String toRFIDTag) {
        this.toRFIDTag = toRFIDTag;
    }
}
