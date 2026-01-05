package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ServiceEntity;
import com.example.multi_tanent.warehouse.model.ServiceRequest;
import com.example.multi_tanent.warehouse.model.ServiceResponse;
import org.springframework.stereotype.Component;

@Component
public class ServiceMapper {

    public ServiceResponse toResponse(ServiceEntity entity) {
        if (entity == null) {
            return null;
        }

        return ServiceResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .defaultUom(entity.getDefaultUom())
                .category(entity.getCategory())
                .active(entity.getActive())
                .build();
    }

    public ServiceEntity toEntity(ServiceRequest request) {
        if (request == null) {
            return null;
        }

        ServiceEntity entity = new ServiceEntity();
        entity.setName(request.name());
        entity.setCode(request.code());
        entity.setDescription(request.description());
        entity.setDefaultUom(request.defaultUom());
        entity.setCategory(request.category());
        entity.setActive(true);

        return entity;
    }

    public void updateEntityFromRequest(ServiceEntity entity, ServiceRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.name() != null) {
            entity.setName(request.name());
        }
        if (request.code() != null) {
            entity.setCode(request.code());
        }
        if (request.description() != null) {
            entity.setDescription(request.description());
        }
        if (request.defaultUom() != null) {
            entity.setDefaultUom(request.defaultUom());
        }
        if (request.category() != null) {
            entity.setCategory(request.category());
        }
    }
}
