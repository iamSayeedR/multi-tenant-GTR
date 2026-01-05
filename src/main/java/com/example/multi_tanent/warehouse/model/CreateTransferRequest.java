package com.example.multi_tanent.warehouse.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTransferRequest {
    @NotNull
    private Long fromLocationId;
    @NotNull
    private Long toLocationId;
    private String remarks;

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

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public List<TransferLineRequest> getLines() {
        return lines;
    }

    public void setLines(List<TransferLineRequest> lines) {
        this.lines = lines;
    }

    private LocalDate issueDate;
    @NotEmpty
    private List<TransferLineRequest> lines;


}
