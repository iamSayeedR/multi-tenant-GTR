package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.GateKeeperEntity;
import com.example.multi_tanent.warehouse.model.GateKeeperRequest;
import com.example.multi_tanent.warehouse.model.GateKeeperResponse;
import org.springframework.stereotype.Component;

@Component
public class GateKeeperMapper {

    public GateKeeperResponse toResponse(GateKeeperEntity entity) {
        if (entity == null) {
            return null;
        }

        GateKeeperResponse response = new GateKeeperResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setEmployeeCode(entity.getEmployeeCode());
        response.setPhone(entity.getPhone());

        if (entity.getWarehouse() != null) {
            response.setWarehouseId(entity.getWarehouse().getId());
            response.setWarehouseName(entity.getWarehouse().getName());
        }

        return response;
    }

    public GateKeeperEntity toEntity(GateKeeperRequest request) {
        if (request == null) {
            return null;
        }

        GateKeeperEntity entity = new GateKeeperEntity();
        entity.setName(request.getName());
        entity.setEmployeeCode(request.getEmployeeCode());
        entity.setPhone(request.getPhone());

        return entity;
    }

    public void updateEntityFromRequest(GateKeeperEntity entity, GateKeeperRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            entity.setName(request.getName());
        }
        if (request.getEmployeeCode() != null) {
            entity.setEmployeeCode(request.getEmployeeCode());
        }
        if (request.getPhone() != null) {
            entity.setPhone(request.getPhone());
        }
    }
}
