package com.example.multi_tanent.warehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "expense_item")
@Data
public class ExpenseItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(unique = true, length = 50)
    private String code;

    @Column(length = 100)
    private String category;

    @Column(nullable = false)
    private Boolean active = true;
}
