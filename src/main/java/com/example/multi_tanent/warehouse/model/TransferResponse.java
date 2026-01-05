package com.example.multi_tanent.warehouse.model;

import com.example.multi_tanent.warehouse.entity.TransferStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransferResponse {

    private Long id;
    private String issueNo;
    private LocalDate issueDate;

    private String fromLocation;
    private String fromLocationName;
    private String fromRFIDTag;

    private String toLocation;
    private String toLocationName;
    private String toRFIDTag;

    private String remarks;
    private TransferStatus status;

    private List<Line> lines;

    public TransferResponse() {
    }

    // Full constructor
    public TransferResponse(Long id, String issueNo, LocalDate issueDate,
            String fromLocation, String fromLocationName, String fromRFIDTag,
            String toLocation, String toLocationName, String toRFIDTag,
            String remarks, TransferStatus status, List<Line> lines) {

        this.id = id;
        this.issueNo = issueNo;
        this.issueDate = issueDate;
        this.fromLocation = fromLocation;
        this.fromLocationName = fromLocationName;
        this.fromRFIDTag = fromRFIDTag;
        this.toLocation = toLocation;
        this.toLocationName = toLocationName;
        this.toRFIDTag = toRFIDTag;
        this.remarks = remarks;
        this.status = status;
        this.lines = lines;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssueNo() {
        return issueNo;
    }

    public void setIssueNo(String issueNo) {
        this.issueNo = issueNo;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getFromLocationName() {
        return fromLocationName;
    }

    public void setFromLocationName(String fromLocationName) {
        this.fromLocationName = fromLocationName;
    }

    public String getFromRFIDTag() {
        return fromRFIDTag;
    }

    public void setFromRFIDTag(String fromRFIDTag) {
        this.fromRFIDTag = fromRFIDTag;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public String getToLocationName() {
        return toLocationName;
    }

    public void setToLocationName(String toLocationName) {
        this.toLocationName = toLocationName;
    }

    public String getToRFIDTag() {
        return toRFIDTag;
    }

    public void setToRFIDTag(String toRFIDTag) {
        this.toRFIDTag = toRFIDTag;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public TransferStatus getStatus() {
        return status;
    }

    public void setStatus(TransferStatus status) {
        this.status = status;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    // Builder Pattern
    public static class Builder {
        private Long id;
        private String issueNo;
        private LocalDate issueDate;

        private String fromLocation;
        private String fromLocationName;
        private String fromRFIDTag;

        private String toLocation;
        private String toLocationName;
        private String toRFIDTag;

        private String remarks;
        private TransferStatus status;

        private List<Line> lines;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder issueNo(String issueNo) {
            this.issueNo = issueNo;
            return this;
        }

        public Builder issueDate(LocalDate issueDate) {
            this.issueDate = issueDate;
            return this;
        }

        public Builder fromLocation(String v) {
            this.fromLocation = v;
            return this;
        }

        public Builder fromLocationName(String v) {
            this.fromLocationName = v;
            return this;
        }

        public Builder fromRFIDTag(String v) {
            this.fromRFIDTag = v;
            return this;
        }

        public Builder toLocation(String v) {
            this.toLocation = v;
            return this;
        }

        public Builder toLocationName(String v) {
            this.toLocationName = v;
            return this;
        }

        public Builder toRFIDTag(String v) {
            this.toRFIDTag = v;
            return this;
        }

        public Builder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public Builder status(TransferStatus status) {
            this.status = status;
            return this;
        }

        public Builder lines(List<Line> lines) {
            this.lines = lines;
            return this;
        }

        public TransferResponse build() {
            return new TransferResponse(
                    id, issueNo, issueDate,
                    fromLocation, fromLocationName, fromRFIDTag,
                    toLocation, toLocationName, toRFIDTag,
                    remarks, status, lines);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    // Line class
    public static class Line {

        private Long lineId;
        private String itemCode;
        private String itemName;

        private Long requisitionQty;
        private Long issuedQty;

        private BigDecimal ratePerUnit;
        private BigDecimal vatPercent;

        public Line() {
        }

        public Line(Long lineId, String itemCode, String itemName,
                Long requisitionQty, Long issuedQty,
                BigDecimal rate, BigDecimal vat) {

            this.lineId = lineId;
            this.itemCode = itemCode;
            this.itemName = itemName;
            this.requisitionQty = requisitionQty;
            this.issuedQty = issuedQty;
            this.ratePerUnit = rate;
            this.vatPercent = vat;
        }

        // ----------------------------
        // Builder Pattern
        // ----------------------------
        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private final Line l = new Line();

            public Builder lineId(Long id) {
                l.setLineId(id);
                return this;
            }

            public Builder itemCode(String code) {
                l.setItemCode(code);
                return this;
            }

            public Builder itemName(String name) {
                l.setItemName(name);
                return this;
            }

            public Builder requisitionQty(Long qty) {
                l.setRequisitionQty(qty);
                return this;
            }

            public Builder issuedQty(Long qty) {
                l.setIssuedQty(qty);
                return this;
            }

            public Builder ratePerUnit(BigDecimal rate) {
                l.setRatePerUnit(rate);
                return this;
            }

            public Builder vatPercent(BigDecimal vat) {
                l.setVatPercent(vat);
                return this;
            }

            public Line build() {
                return l;
            }
        }

        public Long getLineId() {
            return lineId;
        }

        public void setLineId(Long id) {
            this.lineId = id;
        }

        public String getItemCode() {
            return itemCode;
        }

        public void setItemCode(String code) {
            this.itemCode = code;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String name) {
            this.itemName = name;
        }

        public Long getRequisitionQty() {
            return requisitionQty;
        }

        public void setRequisitionQty(Long qty) {
            this.requisitionQty = qty;
        }

        public Long getIssuedQty() {
            return issuedQty;
        }

        public void setIssuedQty(Long qty) {
            this.issuedQty = qty;
        }

        public BigDecimal getRatePerUnit() {
            return ratePerUnit;
        }

        public void setRatePerUnit(BigDecimal rate) {
            this.ratePerUnit = rate;
        }

        public BigDecimal getVatPercent() {
            return vatPercent;
        }

        public void setVatPercent(BigDecimal vat) {
            this.vatPercent = vat;
        }
    }
}
