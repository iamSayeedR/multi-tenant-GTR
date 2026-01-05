package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.RFIDMovementLogEntity;
import com.example.multi_tanent.warehouse.model.RFIDMovementResponse;
import com.example.multi_tanent.warehouse.repository.LocationRepository;
import org.springframework.stereotype.Component;

@Component
public class RFIDMovementMapper {

    private final LocationRepository locationRepository;

    public RFIDMovementMapper(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public RFIDMovementResponse toResponse(RFIDMovementLogEntity entity) {
        if (entity == null) {
            return null;
        }

        RFIDMovementResponse response = new RFIDMovementResponse();
        response.setId(entity.getId());
        response.setTagCode(entity.getTagCode());
        response.setLocationId(entity.getLocationId());
        response.setMovementType(entity.getMovementType());
        if (entity.getTimestamp() != null) {
            response.setTimestamp(entity.getTimestamp().toString());
        }

        if (entity.getLocationId() != null) {
            locationRepository.findById(entity.getLocationId())
                    .ifPresent(loc -> response.setLocationName(loc.getName()));
        }

        return response;
    }
}
