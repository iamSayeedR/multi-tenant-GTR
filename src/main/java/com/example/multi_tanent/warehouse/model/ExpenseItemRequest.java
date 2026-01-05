package com.example.multi_tanent.warehouse.model;

import lombok.Builder;

@Builder
public record ExpenseItemRequest(
                String name,
                String code,
                String category) {
}
