package com.example.multi_tanent.warehouse.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class IssueRecordResponse {

    private Long id;
    private LocalDate date;
    private String toLocation;
    private Long qty;
    private BigDecimal ratePerUnit;
    private BigDecimal vatPercent;
    private BigDecimal amount;

    // Private constructor for builder only
    private IssueRecordResponse(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.toLocation = builder.toLocation;
        this.qty = builder.qty;
        this.ratePerUnit = builder.ratePerUnit;
        this.vatPercent = builder.vatPercent;
        this.amount = builder.amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private LocalDate date;
        private String toLocation;
        private Long qty;
        private BigDecimal ratePerUnit;
        private BigDecimal vatPercent;
        private BigDecimal amount;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder date(LocalDate date) {
            this.date = date;
            return this;
        }

        public Builder toLocation(String toLocation) {
            this.toLocation = toLocation;
            return this;
        }

        public Builder qty(Long qty) {
            this.qty = qty;
            return this;
        }

        public Builder ratePerUnit(BigDecimal ratePerUnit) {
            this.ratePerUnit = ratePerUnit;
            return this;
        }

        public Builder vatPercent(BigDecimal vatPercent) {
            this.vatPercent = vatPercent;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public IssueRecordResponse build() {
            return new IssueRecordResponse(this);
        }
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
