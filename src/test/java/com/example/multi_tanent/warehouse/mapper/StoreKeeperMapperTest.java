package com.example.multi_tanent.warehouse.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.multi_tanent.warehouse.entity.StoreKeeperEntity;
import com.example.multi_tanent.warehouse.entity.StoreKeeperRole;
import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.model.StoreKeeperRequest;
import com.example.multi_tanent.warehouse.model.StoreKeeperResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StoreKeeperMapperTest {

    @InjectMocks
    private StoreKeeperMapper storeKeeperMapper;

    private StoreKeeperEntity storeKeeperEntity;
    private StoreKeeperRequest storeKeeperRequest;
    private WarehouseEntity warehouseEntity;

    @BeforeEach
    void setUp() {
        warehouseEntity = new WarehouseEntity();
        warehouseEntity.setId(1L);
        warehouseEntity.setName("Main Warehouse");

        storeKeeperEntity = new StoreKeeperEntity();
        storeKeeperEntity.setId(1L);
        storeKeeperEntity.setName("John Store");
        storeKeeperEntity.setEmployeeCode("SK-001");
        storeKeeperEntity.setPhone("1234567890");
        storeKeeperEntity.setWarehouse(warehouseEntity);
        storeKeeperEntity.setRole(StoreKeeperRole.STOREKEEPER);
        storeKeeperEntity.setIsAvailable(true);
        storeKeeperEntity.setCurrentWorkload(5);

        storeKeeperRequest = new StoreKeeperRequest();
        storeKeeperRequest.setName("John Store");
        storeKeeperRequest.setEmployeeId("SK-001");
        storeKeeperRequest.setContactNumber("1234567890");
        storeKeeperRequest.setEmail("john@example.com");
        storeKeeperRequest.setWarehouseId(1L);
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        StoreKeeperResponse response = storeKeeperMapper.toResponse(storeKeeperEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("John Store");
        assertThat(response.getEmployeeId()).isEqualTo("SK-001");
        assertThat(response.getContactNumber()).isEqualTo("1234567890");
        assertThat(response.getWarehouseId()).isEqualTo(1L);
        assertThat(response.getWarehouseName()).isEqualTo("Main Warehouse");
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        StoreKeeperResponse response = storeKeeperMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullWarehouse_ShouldHandleGracefully() {
        // Given
        storeKeeperEntity.setWarehouse(null);

        // When
        StoreKeeperResponse response = storeKeeperMapper.toResponse(storeKeeperEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getWarehouseId()).isNull();
        assertThat(response.getWarehouseName()).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        StoreKeeperEntity entity = storeKeeperMapper.toEntity(storeKeeperRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("John Store");
        assertThat(entity.getEmployeeCode()).isEqualTo("SK-001");
        assertThat(entity.getPhone()).isEqualTo("1234567890");
        assertThat(entity.getIsAvailable()).isTrue();
        assertThat(entity.getCurrentWorkload()).isEqualTo(0);
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        StoreKeeperEntity entity = storeKeeperMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateAllFields() {
        // Given
        StoreKeeperRequest updateRequest = new StoreKeeperRequest();
        updateRequest.setName("Jane Store");
        updateRequest.setEmployeeId("SK-002");
        updateRequest.setContactNumber("0987654321");

        // When
        storeKeeperMapper.updateEntityFromRequest(storeKeeperEntity, updateRequest);

        // Then
        assertThat(storeKeeperEntity.getName()).isEqualTo("Jane Store");
        assertThat(storeKeeperEntity.getEmployeeCode()).isEqualTo("SK-002");
        assertThat(storeKeeperEntity.getPhone()).isEqualTo("0987654321");
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = storeKeeperEntity.getName();
        StoreKeeperRequest updateRequest = new StoreKeeperRequest();
        updateRequest.setName(null);
        updateRequest.setEmployeeId(null);
        updateRequest.setContactNumber(null);

        // When
        storeKeeperMapper.updateEntityFromRequest(storeKeeperEntity, updateRequest);

        // Then
        assertThat(storeKeeperEntity.getName()).isEqualTo(originalName);
    }
}
