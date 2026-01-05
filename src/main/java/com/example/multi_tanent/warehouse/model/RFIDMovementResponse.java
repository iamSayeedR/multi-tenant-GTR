package com.example.multi_tanent.warehouse.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RFIDMovementResponse {

    private Long id;
    private String tagCode;
    private Long locationId;
    private String locationName;
    private String movementType;
    private String timestamp;

}
