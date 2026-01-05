package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.EmployeeEntity;
import com.example.multi_tanent.warehouse.model.EmployeeRequest;
import com.example.multi_tanent.warehouse.model.EmployeeResponse;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {

    public EmployeeResponse toResponse(EmployeeEntity entity) {
        if (entity == null) {
            return null;
        }

        return EmployeeResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .employeeCode(entity.getEmployeeCode())
                .departmentId(entity.getDepartment() != null ? entity.getDepartment().getId() : null)
                .departmentName(entity.getDepartment() != null ? entity.getDepartment().getName() : null)
                .email(entity.getEmail())
                .phone(entity.getPhone())
                .active(entity.getActive())
                .build();
    }

    public EmployeeEntity toEntity(EmployeeRequest request) {
        if (request == null) {
            return null;
        }

        EmployeeEntity entity = new EmployeeEntity();
        entity.setName(request.name());
        entity.setEmployeeCode(request.employeeCode());
        entity.setEmail(request.email());
        entity.setPhone(request.phone());
        entity.setActive(true);

        return entity;
    }

    public void updateEntityFromRequest(EmployeeEntity entity, EmployeeRequest request) {
        if (entity == null || request == null) {
            return;
        }

        if (request.name() != null) {
            entity.setName(request.name());
        }
        if (request.employeeCode() != null) {
            entity.setEmployeeCode(request.employeeCode());
        }
        if (request.email() != null) {
            entity.setEmail(request.email());
        }
        if (request.phone() != null) {
            entity.setPhone(request.phone());
        }
    }
}
