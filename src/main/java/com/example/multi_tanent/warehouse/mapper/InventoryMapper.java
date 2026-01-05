package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.InventoryEntity;
import com.example.multi_tanent.warehouse.model.Inventory;
import com.example.multi_tanent.warehouse.model.InventoryResponse;
import org.springframework.stereotype.Component;

@Component
public class InventoryMapper {

    public InventoryResponse toResponse(InventoryEntity entity) {
        if (entity == null) {
            return null;
        }

        InventoryResponse response = new InventoryResponse();
        response.setId(entity.getId());
        response.setLocationId(entity.getLocation() != null ? entity.getLocation().getId() : null);
        response.setLocationName(entity.getLocation() != null ? entity.getLocation().getName() : null);
        response.setItemId(entity.getItem() != null ? entity.getItem().getId() : null);
        response.setItemName(entity.getItem() != null ? entity.getItem().getName() : null);
        response.setQuantity(entity.getQuantity());

        return response;
    }

    public Inventory toDto(InventoryEntity entity) {
        if (entity == null) {
            return null;
        }

        Inventory dto = new Inventory();
        dto.setLocationId(entity.getLocation() != null ? entity.getLocation().getId() : null);
        dto.setItemId(entity.getItem() != null ? entity.getItem().getId() : null);
        dto.setQuantity(entity.getQuantity());

        return dto;
    }

    public InventoryEntity toEntity(Inventory dto) {
        if (dto == null) {
            return null;
        }

        InventoryEntity entity = new InventoryEntity();
        entity.setQuantity(dto.getQuantity());

        return entity;
    }
}
