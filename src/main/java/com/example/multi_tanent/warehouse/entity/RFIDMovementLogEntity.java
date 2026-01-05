package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "rfid_movement_log")
@Getter
@Setter
public class RFIDMovementLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tagCode;

    @Column(name = "location_id")
    private Long locationId;

    @Column(nullable = false)
    private String movementType;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
