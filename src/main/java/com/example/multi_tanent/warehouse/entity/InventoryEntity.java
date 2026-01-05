package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inventory", uniqueConstraints = @UniqueConstraint(columnNames = { "location_id", "item_id" }))
public class InventoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private LocationEntity location;

    @ManyToOne(optional = false)
    private ItemEntity item;

    @Column(nullable = false)
    private Long quantity;
}
