package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gate_out")
@Getter
@Setter
@NoArgsConstructor
public class GateOutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gate_out_no", unique = true, nullable = false)
    private String gateOutNo;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "vehicle_no")
    private String vehicleNo;

    @ManyToOne
    @JoinColumn(name = "gate_keeper_id")
    private GateKeeperEntity gateKeeper;

    @ManyToOne
    @JoinColumn(name = "store_keeper_id")
    private StoreKeeperEntity storeKeeper;

    @Column(name = "remarks")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private GateOutStatus status = GateOutStatus.PENDING;

    // Inter-warehouse transfer tracking
    @ManyToOne
    @JoinColumn(name = "destination_warehouse_id")
    private WarehouseEntity destinationWarehouse;

    // Customer delivery tracking
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_address", length = 500)
    private String customerAddress;

    // Link to corresponding Gate In (for inter-warehouse transfers)
    @ManyToOne
    @JoinColumn(name = "reference_gate_in_id")
    private GateInEntity referenceGateIn;

    @OneToMany(mappedBy = "gateOut", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GateOutLineEntity> lines = new ArrayList<>();
}
