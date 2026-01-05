package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ProjectEntity;
import com.example.multi_tanent.warehouse.model.ProjectRequest;
import com.example.multi_tanent.warehouse.model.ProjectResponse;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

    public ProjectResponse toResponse(ProjectEntity entity) {
        if (entity == null) {
            return null;
        }

        return ProjectResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .budgetAmount(entity.getBudgetAmount())
                .active(entity.getActive())
                .build();
    }

    public ProjectEntity toEntity(ProjectRequest request) {
        if (request == null) {
            return null;
        }

        ProjectEntity entity = new ProjectEntity();
        entity.setName(request.name());
        entity.setCode(request.code());
        entity.setDescription(request.description());
        entity.setStartDate(request.startDate());
        entity.setEndDate(request.endDate());
        entity.setStatus(request.status());
        entity.setBudgetAmount(request.budgetAmount());
        entity.setActive(true);

        return entity;
    }

    public void updateEntityFromRequest(ProjectEntity entity, ProjectRequest request) {
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
        if (request.startDate() != null) {
            entity.setStartDate(request.startDate());
        }
        if (request.endDate() != null) {
            entity.setEndDate(request.endDate());
        }
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
        if (request.budgetAmount() != null) {
            entity.setBudgetAmount(request.budgetAmount());
        }
    }
}
