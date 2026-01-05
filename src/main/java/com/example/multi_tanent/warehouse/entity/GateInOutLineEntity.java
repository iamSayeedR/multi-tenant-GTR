package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gate_in_out_line")
@Getter
@Setter
@NoArgsConstructor
public class GateInOutLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "gate_in_out_id")
    private GateInOutEntity gate;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private Long quantity;
}
