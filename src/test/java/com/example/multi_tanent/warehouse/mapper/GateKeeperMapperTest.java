package com.example.multi_tanent.warehouse.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.multi_tanent.warehouse.entity.GateKeeperEntity;
import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.model.GateKeeperRequest;
import com.example.multi_tanent.warehouse.model.GateKeeperResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GateKeeperMapperTest {

    @InjectMocks
    private GateKeeperMapper gateKeeperMapper;

    private GateKeeperEntity gateKeeperEntity;
    private GateKeeperRequest gateKeeperRequest;
    private WarehouseEntity warehouseEntity;

    @BeforeEach
    void setUp() {
        warehouseEntity = new WarehouseEntity();
        warehouseEntity.setId(1L);
        warehouseEntity.setName("Main Warehouse");

        gateKeeperEntity = new GateKeeperEntity();
        gateKeeperEntity.setId(1L);
        gateKeeperEntity.setName("John Keeper");
        gateKeeperEntity.setEmployeeCode("GK-001");
        gateKeeperEntity.setPhone("1234567890");
        gateKeeperEntity.setWarehouse(warehouseEntity);

        gateKeeperRequest = new GateKeeperRequest();
        gateKeeperRequest.setName("John Keeper");
        gateKeeperRequest.setEmployeeCode("GK-001");
        gateKeeperRequest.setPhone("1234567890");
        gateKeeperRequest.setWarehouseId(1L);
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        GateKeeperResponse response = gateKeeperMapper.toResponse(gateKeeperEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("John Keeper");
        assertThat(response.getEmployeeCode()).isEqualTo("GK-001");
        assertThat(response.getPhone()).isEqualTo("1234567890");
        assertThat(response.getWarehouseId()).isEqualTo(1L);
        assertThat(response.getWarehouseName()).isEqualTo("Main Warehouse");
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        GateKeeperResponse response = gateKeeperMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullWarehouse_ShouldHandleGracefully() {
        // Given
        gateKeeperEntity.setWarehouse(null);

        // When
        GateKeeperResponse response = gateKeeperMapper.toResponse(gateKeeperEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getWarehouseId()).isNull();
        assertThat(response.getWarehouseName()).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        GateKeeperEntity entity = gateKeeperMapper.toEntity(gateKeeperRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("John Keeper");
        assertThat(entity.getEmployeeCode()).isEqualTo("GK-001");
        assertThat(entity.getPhone()).isEqualTo("1234567890");
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        GateKeeperEntity entity = gateKeeperMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateAllFields() {
        // Given
        GateKeeperRequest updateRequest = new GateKeeperRequest();
        updateRequest.setName("Jane Keeper");
        updateRequest.setEmployeeCode("GK-002");
        updateRequest.setPhone("0987654321");

        // When
        gateKeeperMapper.updateEntityFromRequest(gateKeeperEntity, updateRequest);

        // Then
        assertThat(gateKeeperEntity.getName()).isEqualTo("Jane Keeper");
        assertThat(gateKeeperEntity.getEmployeeCode()).isEqualTo("GK-002");
        assertThat(gateKeeperEntity.getPhone()).isEqualTo("0987654321");
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = gateKeeperEntity.getName();
        GateKeeperRequest updateRequest = new GateKeeperRequest();
        updateRequest.setName(null);
        updateRequest.setEmployeeCode(null);
        updateRequest.setPhone(null);

        // When
        gateKeeperMapper.updateEntityFromRequest(gateKeeperEntity, updateRequest);

        // Then
        assertThat(gateKeeperEntity.getName()).isEqualTo(originalName);
    }
}
