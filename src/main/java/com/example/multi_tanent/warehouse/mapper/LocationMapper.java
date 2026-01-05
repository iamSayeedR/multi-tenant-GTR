package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.LocationEntity;
import com.example.multi_tanent.warehouse.model.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationMapper {

    public Location toDto(LocationEntity entity) {
        if (entity == null) {
            return null;
        }

        Location dto = new Location();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getLocationType());
        dto.setCode(entity.getCode());
        dto.setDescription(entity.getDescription());
        dto.setParentId(entity.getParentLocationId());
        dto.setWarehouseId(entity.getWarehouseId());

        return dto;
    }

    public LocationEntity toEntity(Location dto) {
        if (dto == null) {
            return null;
        }

        LocationEntity entity = new LocationEntity();
        entity.setName(dto.getName());
        entity.setLocationType(dto.getType());
        entity.setCode(dto.getCode());
        entity.setDescription(dto.getDescription());
        entity.setParentLocationId(dto.getParentId());
        entity.setWarehouseId(dto.getWarehouseId());

        return entity;
    }

    public void updateEntityFromDto(LocationEntity entity, Location dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getType() != null) {
            entity.setLocationType(dto.getType());
        }
        if (dto.getCode() != null) {
            entity.setCode(dto.getCode());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getParentId() != null) {
            entity.setParentLocationId(dto.getParentId());
        }
        if (dto.getWarehouseId() != null) {
            entity.setWarehouseId(dto.getWarehouseId());
        }
    }
}
