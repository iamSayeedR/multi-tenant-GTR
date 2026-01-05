package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.DepartmentEntity;
import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.model.DepartmentRequest;
import com.example.multi_tanent.warehouse.model.DepartmentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DepartmentMapperTest {

    @InjectMocks
    private DepartmentMapper departmentMapper;

    private DepartmentEntity departmentEntity;
    private DepartmentRequest departmentRequest;
    private WarehouseEntity warehouseEntity;

    @BeforeEach
    void setUp() {
        warehouseEntity = new WarehouseEntity();
        warehouseEntity.setId(1L);
        warehouseEntity.setName("Main Warehouse");

        departmentEntity = new DepartmentEntity();
        departmentEntity.setId(1L);
        departmentEntity.setName("IT Department");
        departmentEntity.setCode("IT-001");
        departmentEntity.setType("DEPARTMENT");
        departmentEntity.setWarehouse(warehouseEntity);
        departmentEntity.setActive(true);

        departmentRequest = DepartmentRequest.builder()
                .name("IT Department")
                .code("IT-001")
                .type("DEPARTMENT")
                .warehouseId(1L)
                .build();
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        DepartmentResponse response = departmentMapper.toResponse(departmentEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("IT Department");
        assertThat(response.getCode()).isEqualTo("IT-001");
        assertThat(response.getType()).isEqualTo("DEPARTMENT");
        assertThat(response.getWarehouseId()).isEqualTo(1L);
        assertThat(response.getWarehouseName()).isEqualTo("Main Warehouse");
        assertThat(response.getActive()).isTrue();
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        DepartmentResponse response = departmentMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullWarehouse_ShouldHandleGracefully() {
        // Given
        departmentEntity.setWarehouse(null);

        // When
        DepartmentResponse response = departmentMapper.toResponse(departmentEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getWarehouseId()).isNull();
        assertThat(response.getWarehouseName()).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        DepartmentEntity entity = departmentMapper.toEntity(departmentRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("IT Department");
        assertThat(entity.getCode()).isEqualTo("IT-001");
        assertThat(entity.getType()).isEqualTo("DEPARTMENT");
        assertThat(entity.getActive()).isTrue();
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        DepartmentEntity entity = departmentMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateAllFields() {
        // Given
        DepartmentRequest updateRequest = DepartmentRequest.builder()
                .name("Updated Department")
                .code("IT-002")
                .type("WAREHOUSE")
                .build();

        // When
        departmentMapper.updateEntityFromRequest(departmentEntity, updateRequest);

        // Then
        assertThat(departmentEntity.getName()).isEqualTo("Updated Department");
        assertThat(departmentEntity.getCode()).isEqualTo("IT-002");
        assertThat(departmentEntity.getType()).isEqualTo("WAREHOUSE");
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = departmentEntity.getName();
        DepartmentRequest updateRequest = DepartmentRequest.builder()
                .name(null)
                .code(null)
                .type(null)
                .build();

        // When
        departmentMapper.updateEntityFromRequest(departmentEntity, updateRequest);

        // Then
        assertThat(departmentEntity.getName()).isEqualTo(originalName);
    }

    @Test
    void updateEntityFromRequest_WithNullEntity_ShouldNotThrowException() {
        // When/Then
        departmentMapper.updateEntityFromRequest(null, departmentRequest);
        // Should not throw exception
    }

    @Test
    void updateEntityFromRequest_WithNullRequest_ShouldNotThrowException() {
        // When/Then
        departmentMapper.updateEntityFromRequest(departmentEntity, null);
        // Should not throw exception
    }
}
