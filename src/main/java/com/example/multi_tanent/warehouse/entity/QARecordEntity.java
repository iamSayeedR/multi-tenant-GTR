package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "qa_record")
public class QARecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_in_id", nullable = false, unique = true)
    private GateInEntity gateIn;

    @Column(name = "driver_rating", nullable = false)
    private Integer driverRating; // 1-5

    @Column(name = "item_quality_rating", nullable = false)
    private Integer itemQualityRating; // 1-5

    @Column(name = "qa_remarks", length = 5000)
    private String qaRemarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspected_by_id", nullable = false)
    private StoreKeeperEntity inspectedBy;

    @Column(name = "inspection_date")
    private LocalDateTime inspectionDate;

    @PrePersist
    protected void onCreate() {
        inspectionDate = LocalDateTime.now();
    }
}
