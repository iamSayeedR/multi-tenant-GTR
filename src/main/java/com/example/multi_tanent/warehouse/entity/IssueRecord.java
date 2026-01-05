package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "")
@Getter
@Setter
@NoArgsConstructor
public class IssueRecord {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InterLocationTransferLine getLine() {
        return line;
    }

    public void setLine(InterLocationTransferLine line) {
        this.line = line;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private InterLocationTransferLine line;
    private LocalDate date;
    private Long qty;
    private BigDecimal ratePerUnit;
    private BigDecimal vatPercent;
    private BigDecimal amount;
}
