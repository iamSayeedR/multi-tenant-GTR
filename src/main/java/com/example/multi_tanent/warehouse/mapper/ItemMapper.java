package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ItemEntity;
import com.example.multi_tanent.warehouse.model.Item;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

    public Item toDto(ItemEntity entity) {
        if (entity == null) {
            return null;
        }

        Item dto = new Item();
        dto.setId(entity.getId());
        dto.setCode(entity.getCode());
        dto.setName(entity.getName());
        dto.setDefaultRate(entity.getDefaultRate());
        dto.setDefaultVatPercent(entity.getDefaultVatPercent());

        return dto;
    }

    public ItemEntity toEntity(Item dto) {
        if (dto == null) {
            return null;
        }

        ItemEntity entity = new ItemEntity();
        entity.setCode(dto.getCode());
        entity.setName(dto.getName());
        entity.setDefaultRate(dto.getDefaultRate());
        entity.setDefaultVatPercent(dto.getDefaultVatPercent());

        return entity;
    }

    public void updateEntityFromDto(ItemEntity entity, Item dto) {
        if (entity == null || dto == null) {
            return;
        }

        if (dto.getCode() != null) {
            entity.setCode(dto.getCode());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDefaultRate() != null) {
            entity.setDefaultRate(dto.getDefaultRate());
        }
        if (dto.getDefaultVatPercent() != null) {
            entity.setDefaultVatPercent(dto.getDefaultVatPercent());
        }
    }
}
