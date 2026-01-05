package com.example.multi_tanent.warehouse.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RFIDMovementRequest {

    private String tagCode;
    private Long locationId;
    private String movementType;
    // expected values: "IN", "OUT", "MOVE"


    public String getTagCode() {
        return tagCode;
    }

    public void setTagCode(String tagCode) {
        this.tagCode = tagCode;
    }

    public Long getLocationId() {
        return locationId;
    }

    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
    }
}
