package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Entity
@Table(name = "project")
@Data
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 50)
    private String code;

    @Column(length = 5000)
    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(length = 20)
    private String status; // PLANNING, ACTIVE, COMPLETED, CANCELLED

    @Column(precision = 15, scale = 2)
    private BigDecimal budgetAmount;

    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<ProjectTaskEntity> tasks = new ArrayList<>();
}
