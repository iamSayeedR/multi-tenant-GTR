package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.StoreKeeperEntity;
import com.example.multi_tanent.warehouse.model.StoreKeeperRequest;
import com.example.multi_tanent.warehouse.model.StoreKeeperResponse;
import org.springframework.stereotype.Component;

@Component
public class StoreKeeperMapper {

    public StoreKeeperResponse toResponse(StoreKeeperEntity entity) {
        if (entity == null) {
            return null;
        }

        StoreKeeperResponse response = new StoreKeeperResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmployeeId(entity.getEmployeeCode());
        response.setContactNumber(entity.getPhone());
        response.setEmail(entity.getEmail());

        if (entity.getWarehouse() != null) {
            response.setWarehouseId(entity.getWarehouse().getId());
            response.setWarehouseName(entity.getWarehouse().getName());
        }

        return response;
    }

    public StoreKeeperEntity toEntity(StoreKeeperRequest request) {
        if (request == null) {
            return null;
        }

        StoreKeeperEntity entity = new StoreKeeperEntity();
        entity.setName(request.getName());
        entity.setEmployeeCode(request.getEmployeeId());
        entity.setPhone(request.getContactNumber());
        entity.setEmail(request.getEmail());
        entity.setIsAvailable(true);
        entity.setCurrentWorkload(0);

        return entity;
    }

    public void updateEntityFromRequest(StoreKeeperEntity entity, StoreKeeperRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getEmployeeId() != null) {
            entity.setEmployeeCode(request.getEmployeeId());
        }
        if (request.getContactNumber() != null) {
            entity.setPhone(request.getContactNumber());
        }
        if (request.getEmail() != null) {
            entity.setEmail(request.getEmail());
        }
    }
}
