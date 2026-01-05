package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ExpenseItemEntity;
import com.example.multi_tanent.warehouse.model.ExpenseItemRequest;
import com.example.multi_tanent.warehouse.model.ExpenseItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ExpenseItemMapperTest {

    @InjectMocks
    private ExpenseItemMapper expenseItemMapper;

    private ExpenseItemEntity expenseItemEntity;
    private ExpenseItemRequest expenseItemRequest;

    @BeforeEach
    void setUp() {
        expenseItemEntity = new ExpenseItemEntity();
        expenseItemEntity.setId(1L);
        expenseItemEntity.setName("Office Supplies");
        expenseItemEntity.setCode("EXP-001");
        expenseItemEntity.setCategory("Stationery");
        expenseItemEntity.setActive(true);

        expenseItemRequest = ExpenseItemRequest.builder()
                .name("Office Supplies")
                .code("EXP-001")
                .category("Stationery")
                .build();
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        ExpenseItemResponse response = expenseItemMapper.toResponse(expenseItemEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Office Supplies");
        assertThat(response.getCode()).isEqualTo("EXP-001");
        assertThat(response.getCategory()).isEqualTo("Stationery");
        assertThat(response.getActive()).isTrue();
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        ExpenseItemResponse response = expenseItemMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        ExpenseItemEntity entity = expenseItemMapper.toEntity(expenseItemRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Office Supplies");
        assertThat(entity.getCode()).isEqualTo("EXP-001");
        assertThat(entity.getCategory()).isEqualTo("Stationery");
        assertThat(entity.getActive()).isTrue();
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        ExpenseItemEntity entity = expenseItemMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateAllFields() {
        // Given
        ExpenseItemRequest updateRequest = ExpenseItemRequest.builder()
                .name("Updated Supplies")
                .code("EXP-002")
                .category("Equipment")
                .build();

        // When
        expenseItemMapper.updateEntityFromRequest(expenseItemEntity, updateRequest);

        // Then
        assertThat(expenseItemEntity.getName()).isEqualTo("Updated Supplies");
        assertThat(expenseItemEntity.getCode()).isEqualTo("EXP-002");
        assertThat(expenseItemEntity.getCategory()).isEqualTo("Equipment");
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = expenseItemEntity.getName();
        ExpenseItemRequest updateRequest = ExpenseItemRequest.builder()
                .name(null)
                .code(null)
                .category(null)
                .build();

        // When
        expenseItemMapper.updateEntityFromRequest(expenseItemEntity, updateRequest);

        // Then
        assertThat(expenseItemEntity.getName()).isEqualTo(originalName);
    }

    @Test
    void updateEntityFromRequest_WithNullEntity_ShouldNotThrowException() {
        // When/Then
        expenseItemMapper.updateEntityFromRequest(null, expenseItemRequest);
        // Should not throw exception
    }

    @Test
    void updateEntityFromRequest_WithNullRequest_ShouldNotThrowException() {
        // When/Then
        expenseItemMapper.updateEntityFromRequest(expenseItemEntity, null);
        // Should not throw exception
    }
}
