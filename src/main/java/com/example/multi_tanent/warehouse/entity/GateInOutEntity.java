package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gate_in_out")
@Getter
@Setter
@NoArgsConstructor
public class GateInOutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String gateNo;
    private String truckNo;
    private String driverName;

    @Enumerated(EnumType.STRING)
    private GateMovementType movementType; // IN or OUT

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "gate_keeper_id")
    private GateKeeperEntity gateKeeper;

    @OneToMany(mappedBy = "gate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GateInOutLineEntity> lines = new ArrayList<>();
}
