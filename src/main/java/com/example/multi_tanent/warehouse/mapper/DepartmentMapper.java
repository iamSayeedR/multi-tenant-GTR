package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.DepartmentEntity;
import com.example.multi_tanent.warehouse.model.DepartmentRequest;
import com.example.multi_tanent.warehouse.model.DepartmentResponse;
import org.springframework.stereotype.Component;

@Component
public class DepartmentMapper {

    public DepartmentResponse toResponse(DepartmentEntity entity) {
        if (entity == null) {
            return null;
        }

        return DepartmentResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .type(entity.getType())
                .warehouseId(entity.getWarehouse() != null ? entity.getWarehouse().getId() : null)
                .warehouseName(entity.getWarehouse() != null ? entity.getWarehouse().getName() : null)
                .active(entity.getActive())
                .build();
    }

    public DepartmentEntity toEntity(DepartmentRequest request) {
        if (request == null) {
            return null;
        }

        DepartmentEntity entity = new DepartmentEntity();
        entity.setName(request.name());
        entity.setCode(request.code());
        entity.setType(request.type());
        entity.setActive(true);
        
        return entity;
    }

    public void updateEntityFromRequest(DepartmentEntity entity, DepartmentRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.name() != null) {
            entity.setName(request.name());
        }
        if (request.code() != null) {
            entity.setCode(request.code());
        }
        if (request.type() != null) {
            entity.setType(request.type());
        }
    }
}
