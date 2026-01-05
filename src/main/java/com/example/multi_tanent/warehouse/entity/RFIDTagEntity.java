package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "rfid_tag")
public class RFIDTagEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_code", unique = true, nullable = false)
    private String tagCode;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    @Column(name = "active")
    private Boolean active = true;
}
