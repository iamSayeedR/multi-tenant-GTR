package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "location")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "location_type")
    private String locationType;

    @Column(name = "parent_location_id")
    private Long parentLocationId;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;
}
