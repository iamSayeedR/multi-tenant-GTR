package com.example.multi_tanent.warehouse.model;

import com.example.multi_tanent.warehouse.entity.GateOutReason;
import lombok.Data;

@Data
public class GateOutLineResponse {
    private Long id;
    private Long itemId;
    private String itemName;
    private String rfidTagCode;
    private Long qty;
    private Long fromLocationId;
    private String fromLocationName;
    private GateOutReason reason;
}
