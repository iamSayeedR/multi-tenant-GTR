package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ExpenseItemEntity;
import com.example.multi_tanent.warehouse.model.ExpenseItemRequest;
import com.example.multi_tanent.warehouse.model.ExpenseItemResponse;
import org.springframework.stereotype.Component;

@Component
public class ExpenseItemMapper {

    public ExpenseItemResponse toResponse(ExpenseItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return ExpenseItemResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .category(entity.getCategory())
                .active(entity.getActive())
                .build();
    }

    public ExpenseItemEntity toEntity(ExpenseItemRequest request) {
        if (request == null) {
            return null;
        }

        ExpenseItemEntity entity = new ExpenseItemEntity();
        entity.setName(request.name());
        entity.setCode(request.code());
        entity.setCategory(request.category());
        entity.setActive(true);

        return entity;
    }

    public void updateEntityFromRequest(ExpenseItemEntity entity, ExpenseItemRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.name() != null) {
            entity.setName(request.name());
        }
        if (request.code() != null) {
            entity.setCode(request.code());
        }
        if (request.category() != null) {
            entity.setCategory(request.category());
        }
    }
}
