package com.example.multi_tanent.warehouse.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.multi_tanent.warehouse.entity.*;
import com.example.multi_tanent.warehouse.model.GateInLineResponse;
import com.example.multi_tanent.warehouse.model.GateInResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GateInMapperTest {

    @InjectMocks
    private GateInMapper gateInMapper;

    private GateInEntity gateInEntity;
    private GateInLineEntity gateInLineEntity;
    private GateKeeperEntity gateKeeperEntity;
    private StoreKeeperEntity storeKeeperEntity;
    private WarehouseEntity warehouseEntity;
    private ItemEntity itemEntity;
    private LocationEntity locationEntity;

    @BeforeEach
    void setUp() {
        warehouseEntity = new WarehouseEntity();
        warehouseEntity.setId(1L);
        warehouseEntity.setName("Main Warehouse");

        gateKeeperEntity = new GateKeeperEntity();
        gateKeeperEntity.setId(1L);
        gateKeeperEntity.setName("John Keeper");

        storeKeeperEntity = new StoreKeeperEntity();
        storeKeeperEntity.setId(1L);
        storeKeeperEntity.setName("Jane Store");

        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("Widget");

        locationEntity = new LocationEntity();
        locationEntity.setId(1L);
        locationEntity.setName("Bin A-01");

        gateInLineEntity = new GateInLineEntity();
        gateInLineEntity.setId(1L);
        gateInLineEntity.setItem(itemEntity);
        gateInLineEntity.setRfidTagCode("RFID-123");
        gateInLineEntity.setQty(100L);
        gateInLineEntity.setToLocation(locationEntity);

        gateInEntity = new GateInEntity();
        gateInEntity.setId(1L);
        gateInEntity.setGateInNo("GIN-001");
        gateInEntity.setTimestamp(LocalDateTime.of(2024, 1, 1, 10, 0));
        gateInEntity.setVehicleNo("ABC-123");
        gateInEntity.setRemarks("Test gate in");
        gateInEntity.setStatus(GateInStatus.PENDING);
        gateInEntity.setGateKeeper(gateKeeperEntity);
        gateInEntity.setStoreKeeper(storeKeeperEntity);
        gateInEntity.setSourceWarehouse(warehouseEntity);
        gateInEntity.setSupplierName("Test Supplier");
        gateInEntity.setSupplierAddress("123 Supplier St");
        gateInEntity.setLines(Arrays.asList(gateInLineEntity));
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        GateInResponse response = gateInMapper.toResponse(gateInEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getGateInNo()).isEqualTo("GIN-001");
        assertThat(response.getTimestamp()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0));
        assertThat(response.getVehicleNo()).isEqualTo("ABC-123");
        assertThat(response.getRemarks()).isEqualTo("Test gate in");
        assertThat(response.getStatus()).isEqualTo(GateInStatus.PENDING);
        assertThat(response.getGateKeeperId()).isEqualTo(1L);
        assertThat(response.getGateKeeperName()).isEqualTo("John Keeper");
        assertThat(response.getStoreKeeperId()).isEqualTo(1L);
        assertThat(response.getStoreKeeperName()).isEqualTo("Jane Store");
        assertThat(response.getSourceWarehouseId()).isEqualTo(1L);
        assertThat(response.getSourceWarehouseName()).isEqualTo("Main Warehouse");
        assertThat(response.getSupplierName()).isEqualTo("Test Supplier");
        assertThat(response.getSupplierAddress()).isEqualTo("123 Supplier St");
        assertThat(response.getLines()).hasSize(1);
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        GateInResponse response = gateInMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullGateKeeper_ShouldHandleGracefully() {
        // Given
        gateInEntity.setGateKeeper(null);

        // When
        GateInResponse response = gateInMapper.toResponse(gateInEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getGateKeeperId()).isNull();
        assertThat(response.getGateKeeperName()).isNull();
    }

    @Test
    void toResponse_WithNullStoreKeeper_ShouldHandleGracefully() {
        // Given
        gateInEntity.setStoreKeeper(null);

        // When
        GateInResponse response = gateInMapper.toResponse(gateInEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStoreKeeperId()).isNull();
        assertThat(response.getStoreKeeperName()).isNull();
    }

    @Test
    void toResponse_WithNullSourceWarehouse_ShouldHandleGracefully() {
        // Given
        gateInEntity.setSourceWarehouse(null);

        // When
        GateInResponse response = gateInMapper.toResponse(gateInEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getSourceWarehouseId()).isNull();
        assertThat(response.getSourceWarehouseName()).isNull();
    }

    @Test
    void mapLineToResponse_ShouldMapLineEntityToResponse() {
        // When
        GateInLineResponse lineResponse = gateInMapper.mapLineToResponse(gateInLineEntity);

        // Then
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getItemId()).isEqualTo(1L);
        assertThat(lineResponse.getItemName()).isEqualTo("Widget");
        assertThat(lineResponse.getRfidTagCode()).isEqualTo("RFID-123");
        assertThat(lineResponse.getQty()).isEqualTo(100L);
        assertThat(lineResponse.getToLocationId()).isEqualTo(1L);
        assertThat(lineResponse.getToLocationName()).isEqualTo("Bin A-01");
    }

    @Test
    void mapLineToResponse_WithNullLine_ShouldReturnNull() {
        // When
        GateInLineResponse lineResponse = gateInMapper.mapLineToResponse(null);

        // Then
        assertThat(lineResponse).isNull();
    }

    @Test
    void mapLineToResponse_WithNullLocation_ShouldHandleGracefully() {
        // Given
        gateInLineEntity.setToLocation(null);

        // When
        GateInLineResponse lineResponse = gateInMapper.mapLineToResponse(gateInLineEntity);

        // Then
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getToLocationId()).isNull();
        assertThat(lineResponse.getToLocationName()).isNull();
    }
}
