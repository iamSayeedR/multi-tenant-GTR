package com.example.multi_tanent.warehouse.model;

import java.util.List;

public class TransferRequest {

    private Long fromLocationId;
    private Long toLocationId;

    private String fromRFIDTag;
    private String toRFIDTag;

    private String remarks;

    private List<TransferLineRequest> lines;

    public Long getFromLocationId() {
        return fromLocationId;
    }

    public void setFromLocationId(Long fromLocationId) {
        this.fromLocationId = fromLocationId;
    }

    public Long getToLocationId() {
        return toLocationId;
    }

    public void setToLocationId(Long toLocationId) {
        this.toLocationId = toLocationId;
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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<TransferLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<TransferLineRequest> lines) {
        this.lines = lines;
    }
}
