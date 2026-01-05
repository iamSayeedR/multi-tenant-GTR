package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "service")
@Data
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 50)
    private String code;

    @Column(length = 5000)
    private String description;

    @Column(length = 20)
    private String defaultUom;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private Boolean active = true;
}
