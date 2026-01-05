package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "requisition")
@Data
public class RequisitionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String requisitionNo;

    @ManyToOne
    @JoinColumn(name = "entity_id")
    private CompanyEntityEntity entity;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private DepartmentEntity recipient;

    @ManyToOne
    @JoinColumn(name = "requested_by_id", nullable = false)
    private EmployeeEntity requestedBy;

    @Column(nullable = false)
    private LocalDate requiredDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private RequisitionStatus status = RequisitionStatus.DRAFT;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(name = "customer_order_id")
    private Long customerOrderId; // Link to customer order if exists

    @Column(length = 50)
    private String basisType; // CUSTOMER_ORDER, PRODUCTION_ORDER, PROJECT_ESTIMATE

    private Long basisId;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    @ManyToOne
    @JoinColumn(name = "project_task_id")
    private ProjectTaskEntity projectTask;

    @Column(length = 5000)
    private String comment;

    @Column(precision = 15, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(name = "created_by_id")
    private Long createdById;

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitionLineEntity> lines = new ArrayList<>();

    @OneToMany(mappedBy = "requisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequisitionAdditionalInfoEntity> additionalInfo = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
