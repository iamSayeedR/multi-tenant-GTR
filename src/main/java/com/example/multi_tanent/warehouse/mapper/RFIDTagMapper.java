package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.RFIDTagEntity;
import com.example.multi_tanent.warehouse.model.RFIDTagRequest;
import com.example.multi_tanent.warehouse.model.RFIDTagResponse;
import org.springframework.stereotype.Component;

@Component
public class RFIDTagMapper {

    public RFIDTagResponse toResponse(RFIDTagEntity entity) {
        if (entity == null) {
            return null;
        }

        RFIDTagResponse response = new RFIDTagResponse();
        response.setId(entity.getId());
        response.setTagCode(entity.getTagCode());
        response.setActive(entity.getActive());

        if (entity.getItem() != null) {
            response.setItemId(entity.getItem().getId());
            response.setItemCode(entity.getItem().getCode());
            response.setItemName(entity.getItem().getName());
        }

        return response;
    }

    public RFIDTagEntity toEntity(RFIDTagRequest request) {
        if (request == null) {
            return null;
        }

        RFIDTagEntity entity = new RFIDTagEntity();
        entity.setTagCode(request.getTagCode());
        entity.setActive(true);

        return entity;
    }

    public void updateEntityFromRequest(RFIDTagEntity entity, RFIDTagRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.getTagCode() != null) {
            entity.setTagCode(request.getTagCode());
        }
    }
}
