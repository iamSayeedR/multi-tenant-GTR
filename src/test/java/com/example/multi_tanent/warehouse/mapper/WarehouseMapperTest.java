package com.example.multi_tanent.warehouse.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.multi_tanent.spersusers.enitity.Tenant;
import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.model.WarehouseRequest;
import com.example.multi_tanent.warehouse.model.WarehouseResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WarehouseMapperTest {

    @InjectMocks
    private WarehouseMapper warehouseMapper;

    private WarehouseEntity warehouseEntity;
    private WarehouseRequest warehouseRequest;
    private Tenant tenant;

    @BeforeEach
    void setUp() {
        tenant = new Tenant();
        tenant.setId(1L);
        tenant.setName("Test Tenant");

        warehouseEntity = new WarehouseEntity();
        warehouseEntity.setId(1L);
        warehouseEntity.setName("Main Warehouse");
        warehouseEntity.setAddress("123 Main St");
        warehouseEntity.setActive(true);
        warehouseEntity.setTenant(tenant);

        warehouseRequest = new WarehouseRequest();
        warehouseRequest.setName("Main Warehouse");
        warehouseRequest.setAddress("123 Main St");
        warehouseRequest.setActive(true);
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        WarehouseResponse response = warehouseMapper.toResponse(warehouseEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Main Warehouse");
        assertThat(response.getAddress()).isEqualTo("123 Main St");
        assertThat(response.getActive()).isTrue();
        assertThat(response.getTenantId()).isEqualTo(1L);
        assertThat(response.getTenantName()).isEqualTo("Test Tenant");
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        WarehouseResponse response = warehouseMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullTenant_ShouldHandleGracefully() {
        // Given
        warehouseEntity.setTenant(null);

        // When
        WarehouseResponse response = warehouseMapper.toResponse(warehouseEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getTenantId()).isNull();
        assertThat(response.getTenantName()).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        WarehouseEntity entity = warehouseMapper.toEntity(warehouseRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Main Warehouse");
        assertThat(entity.getAddress()).isEqualTo("123 Main St");
        assertThat(entity.getActive()).isTrue();
    }

    @Test
    void toEntity_WithNullActive_ShouldDefaultToTrue() {
        // Given
        warehouseRequest.setActive(null);

        // When
        WarehouseEntity entity = warehouseMapper.toEntity(warehouseRequest);

        // Then
        assertThat(entity.getActive()).isTrue();
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        WarehouseEntity entity = warehouseMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateAllFields() {
        // Given
        WarehouseRequest updateRequest = new WarehouseRequest();
        updateRequest.setName("Updated Warehouse");
        updateRequest.setAddress("456 New St");
        updateRequest.setActive(false);

        // When
        warehouseMapper.updateEntityFromRequest(warehouseEntity, updateRequest);

        // Then
        assertThat(warehouseEntity.getName()).isEqualTo("Updated Warehouse");
        assertThat(warehouseEntity.getAddress()).isEqualTo("456 New St");
        assertThat(warehouseEntity.getActive()).isFalse();
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = warehouseEntity.getName();
        WarehouseRequest updateRequest = new WarehouseRequest();
        updateRequest.setName(null);
        updateRequest.setAddress(null);
        updateRequest.setActive(null);

        // When
        warehouseMapper.updateEntityFromRequest(warehouseEntity, updateRequest);

        // Then
        assertThat(warehouseEntity.getName()).isEqualTo(originalName);
    }
}
