package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "employee")
@Data
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 50)
    private String employeeCode;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;

    @Column(length = 200)
    private String email;

    @Column(length = 50)
    private String phone;

    @Column(nullable = false)
    private Boolean active = true;
}
