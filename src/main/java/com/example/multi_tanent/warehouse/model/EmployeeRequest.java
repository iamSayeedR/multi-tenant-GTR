package com.example.multi_tanent.warehouse.model;

import lombok.Builder;

@Builder
public record EmployeeRequest(
        String name,
        String employeeCode,
        Long departmentId,
        String email,
        String phone) {
}
