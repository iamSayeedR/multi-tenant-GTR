package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "company_entity")
@Data
public class CompanyEntityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 50)
    private String code;

    @Column(nullable = false)
    private Boolean active = true;
}
