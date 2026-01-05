package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gate_in_line")
@Getter
@Setter
@NoArgsConstructor
public class GateInLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gate_in_id", nullable = false)
    private GateInEntity gateIn;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Column(name = "rfid_tag_code")
    private String rfidTagCode;

    @Column(name = "qty", nullable = false)
    private Long qty;

    // Only toLocation is needed for Gate In (goods come from suppliers, not
    // warehouse locations)
    @ManyToOne
    @JoinColumn(name = "to_location_id")
    private LocationEntity toLocation;
}
