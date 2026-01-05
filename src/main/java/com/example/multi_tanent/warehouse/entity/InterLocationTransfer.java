package com.example.multi_tanent.warehouse.entity;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inter_location_transfer")
@Getter
@Setter
@NoArgsConstructor
public class InterLocationTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String issueNo;
    private LocalDate issueDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "from_location_id", nullable = false)
    private LocationEntity fromLocation;
    @ManyToOne(optional = false)
    @JoinColumn(name = "to_location_id", nullable = false)
    private LocationEntity toLocation;
    private String remarks;
    @Enumerated(EnumType.STRING)
    private TransferStatus status = TransferStatus.PENDING;
    @Column(name = "from_rfid_tag")
    private String fromRFIDTag;
    @Column(name = "to_rfid_tag")
    private String toRFIDTag;
    @OneToMany(mappedBy = "transfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InterLocationTransferLine> lines = new ArrayList<>();

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

    public LocationEntity getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(LocationEntity fromLocation) {
        this.fromLocation = fromLocation;
    }

    public LocationEntity getToLocation() {
        return toLocation;
    }

    public void setToLocation(LocationEntity toLocation) {
        this.toLocation = toLocation;
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

    public List<InterLocationTransferLine> getLines() {
        return lines;
    }

    public void setLines(List<InterLocationTransferLine> lines) {
        this.lines = lines;
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
