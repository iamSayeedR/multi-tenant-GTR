package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "shipment_schedule")
public class ShipmentScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_no", unique = true, nullable = false)
    private String scheduleNo;

    @Column(name = "expected_arrival_date", nullable = false)
    private LocalDate expectedArrivalDate;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "truck_number")
    private String truckNumber;

    @Column(name = "supplier_name")
    private String supplierName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private WarehouseEntity warehouse;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_in_id")
    private GateInEntity gateIn;

    @Column(name = "status", nullable = false)
    private String status = "SCHEDULED"; // SCHEDULED, ARRIVED, COMPLETED, CANCELLED

    @Column(name = "remarks", length = 5000)
    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "shipmentSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShipmentScheduleLineEntity> lines = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
