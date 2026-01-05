package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gate_in")
@Getter
@Setter
@NoArgsConstructor
public class GateInEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gate_in_no", unique = true, nullable = false)
    private String gateInNo;

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
    private GateInStatus status = GateInStatus.PENDING;

    // Inter-warehouse transfer tracking
    @ManyToOne
    @JoinColumn(name = "source_warehouse_id")
    private WarehouseEntity sourceWarehouse;

    // Supplier purchase tracking
    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "supplier_address", length = 500)
    private String supplierAddress;

    // Link to corresponding Gate Out (for inter-warehouse transfers)
    @ManyToOne
    @JoinColumn(name = "reference_gate_out_id")
    private GateOutEntity referenceGateOut;

    @OneToMany(mappedBy = "gateIn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GateInLineEntity> lines = new ArrayList<>();
}
