package com.example.multi_tanent.warehouse.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferLineResponse {

    private Long itemId;
    private String itemCode;
    private String itemName;

    private Long requisitionQty;
    private Long issuedQty;
    private Double ratePerUnit;
    private Double vatPercent;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public Double getRatePerUnit() {
        return ratePerUnit;
    }

    public void setRatePerUnit(Double ratePerUnit) {
        this.ratePerUnit = ratePerUnit;
    }

    public Double getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(Double vatPercent) {
        this.vatPercent = vatPercent;
    }
}
