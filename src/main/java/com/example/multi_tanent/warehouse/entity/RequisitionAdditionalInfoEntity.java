package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "requisition_additional_info")
@Data
public class RequisitionAdditionalInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "requisition_id", nullable = false)
    private RequisitionEntity requisition;

    @Column(nullable = false, length = 100)
    private String fieldName;

    @Column(length = 5000)
    private String fieldValue;
}
