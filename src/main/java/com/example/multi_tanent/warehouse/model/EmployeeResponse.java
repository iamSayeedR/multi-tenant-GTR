package com.example.multi_tanent.warehouse.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeResponse {
    private Long id;
    private String name;
    private String employeeCode;
    private Long departmentId;
    private String departmentName;
    private String email;
    private String phone;
    private Boolean active;
}
