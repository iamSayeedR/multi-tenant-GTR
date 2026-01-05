package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "storage_assignment")
public class StorageAssignmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assignment_no", unique = true, nullable = false)
    private String assignmentNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gate_in_line_id", nullable = false)
    private GateInLineEntity gateInLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id", nullable = false)
    private StoreKeeperEntity assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_location_id", nullable = false)
    private LocationEntity targetLocation;

    @Column(name = "quantity_to_store", nullable = false)
    private Long quantityToStore;

    @Column(name = "status", nullable = false)
    private String status = "ASSIGNED"; // ASSIGNED, IN_PROGRESS, COMPLETED

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "completion_remarks", length = 5000)
    private String completionRemarks;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }
}
