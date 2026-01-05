package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Table(name = "project_task")
@Data
public class ProjectTaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 5000)
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = 20)
    private String status; // NOT_STARTED, IN_PROGRESS, COMPLETED, CANCELLED

    @Column(precision = 15, scale = 2)
    private BigDecimal budgetAmount;
}
