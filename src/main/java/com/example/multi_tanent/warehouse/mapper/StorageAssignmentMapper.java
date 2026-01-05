package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.LocationEntity;
import com.example.multi_tanent.warehouse.entity.StorageAssignmentEntity;
import com.example.multi_tanent.warehouse.model.StorageAssignmentResponse;
import com.example.multi_tanent.warehouse.repository.LocationRepository;
import org.springframework.stereotype.Component;

@Component
public class StorageAssignmentMapper {

    private final LocationRepository locationRepository;

    public StorageAssignmentMapper(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public StorageAssignmentResponse toResponse(StorageAssignmentEntity entity) {
        if (entity == null) {
            return null;
        }

        // Get location hierarchy
        LocationEntity bin = entity.getTargetLocation();
        LocationEntity rack = bin != null && bin.getParentLocationId() != null
                ? locationRepository.findById(bin.getParentLocationId()).orElse(null)
                : null;
        LocationEntity floor = rack != null && rack.getParentLocationId() != null
                ? locationRepository.findById(rack.getParentLocationId()).orElse(null)
                : null;
        LocationEntity zone = floor != null && floor.getParentLocationId() != null
                ? locationRepository.findById(floor.getParentLocationId()).orElse(null)
                : null;

        return StorageAssignmentResponse.builder()
                .id(entity.getId())
                .assignmentNo(entity.getAssignmentNo())
                .gateInLineId(entity.getGateInLine().getId())
                .itemId(entity.getGateInLine().getItem().getId())
                .itemName(entity.getGateInLine().getItem().getName())
                .assignedToId(entity.getAssignedTo().getId())
                .assignedToName(entity.getAssignedTo().getName())
                .assignedToCode(entity.getAssignedTo().getEmployeeCode())
                .targetLocationId(entity.getTargetLocation() != null ? entity.getTargetLocation().getId() : null)
                .binName(bin != null ? bin.getName() : null)
                .rackName(rack != null ? rack.getName() : null)
                .floorName(floor != null ? floor.getName() : null)
                .zoneName(zone != null ? zone.getName() : null)
                .quantityToStore(entity.getQuantityToStore())
                .status(entity.getStatus())
                .assignedAt(entity.getAssignedAt())
                .completedAt(entity.getCompletedAt())
                .completionRemarks(entity.getCompletionRemarks())
                .build();
    }
}
