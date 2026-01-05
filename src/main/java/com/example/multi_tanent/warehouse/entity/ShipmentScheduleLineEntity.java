package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "shipment_schedule_line")
public class ShipmentScheduleLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_schedule_id", nullable = false)
    private ShipmentScheduleEntity shipmentSchedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Column(name = "expected_quantity", nullable = false)
    private Long expectedQuantity;
}
