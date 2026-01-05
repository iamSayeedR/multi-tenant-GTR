package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.model.WarehouseRequest;
import com.example.multi_tanent.warehouse.model.WarehouseResponse;
import org.springframework.stereotype.Component;

@Component
public class WarehouseMapper {

    public WarehouseResponse toResponse(WarehouseEntity entity) {
        if (entity == null) {
            return null;
        }

        WarehouseResponse response = new WarehouseResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setAddress(entity.getAddress());
        response.setActive(entity.getActive());

        if (entity.getTenant() != null) {
            response.setTenantId(entity.getTenant().getId());
            response.setTenantName(entity.getTenant().getName());
        }

        return response;
    }

    public WarehouseEntity toEntity(WarehouseRequest request) {
        if (request == null) {
            return null;
        }

        WarehouseEntity entity = new WarehouseEntity();
        entity.setName(request.getName());
        entity.setAddress(request.getAddress());
        entity.setActive(request.getActive() != null ? request.getActive() : true);

        return entity;
    }

    public void updateEntityFromRequest(WarehouseEntity entity, WarehouseRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getAddress() != null) {
            entity.setAddress(request.getAddress());
        }
        if (request.getActive() != null) {
            entity.setActive(request.getActive());
        }
    }
}
