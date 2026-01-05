package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gate_out_line")
@Getter
@Setter
@NoArgsConstructor
public class GateOutLineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gate_out_id", nullable = false)
    private GateOutEntity gateOut;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;

    @Column(name = "rfid_tag_code")
    private String rfidTagCode;

    @Column(name = "qty", nullable = false)
    private Long qty;

    @ManyToOne(optional = false)
    @JoinColumn(name = "from_location_id", nullable = false)
    private LocationEntity fromLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private GateOutReason reason;
}
