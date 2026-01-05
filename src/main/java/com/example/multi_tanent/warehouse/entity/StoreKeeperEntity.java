package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "store_keeper")
public class StoreKeeperEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "employee_code", unique = true, nullable = false)
    private String employeeCode;

    private String phone;

    private String email;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private WarehouseEntity warehouse;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private StoreKeeperRole role = StoreKeeperRole.STOREKEEPER;

    @ManyToOne
    @JoinColumn(name = "assigned_floor_id")
    private LocationEntity assignedFloor;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "current_workload")
    private Integer currentWorkload = 0;

    // Manager relationship
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private StoreKeeperEntity manager; // The manager who supervises this storekeeper

    @OneToMany(mappedBy = "manager")
    private List<StoreKeeperEntity> team; // Storekeepers managed by this manager (if this is a manager)
}
