package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.*;
import com.example.multi_tanent.warehouse.model.GateOutLineResponse;
import com.example.multi_tanent.warehouse.model.GateOutResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GateOutMapperTest {

    @InjectMocks
    private GateOutMapper gateOutMapper;

    private GateOutEntity gateOutEntity;
    private GateOutLineEntity gateOutLineEntity;
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

        gateOutLineEntity = new GateOutLineEntity();
        gateOutLineEntity.setId(1L);
        gateOutLineEntity.setItem(itemEntity);
        gateOutLineEntity.setRfidTagCode("RFID-123");
        gateOutLineEntity.setQty(50L);
        gateOutLineEntity.setFromLocation(locationEntity);
        gateOutLineEntity.setReason(GateOutReason.SALES);

        gateOutEntity = new GateOutEntity();
        gateOutEntity.setId(1L);
        gateOutEntity.setGateOutNo("GOUT-001");
        gateOutEntity.setTimestamp(LocalDateTime.of(2024, 1, 1, 14, 0));
        gateOutEntity.setVehicleNo("XYZ-789");
        gateOutEntity.setRemarks("Test gate out");
        gateOutEntity.setStatus(GateOutStatus.PENDING);
        gateOutEntity.setGateKeeper(gateKeeperEntity);
        gateOutEntity.setStoreKeeper(storeKeeperEntity);
        gateOutEntity.setDestinationWarehouse(warehouseEntity);
        gateOutEntity.setCustomerName("Test Customer");
        gateOutEntity.setCustomerAddress("456 Customer Ave");
        gateOutEntity.setLines(Arrays.asList(gateOutLineEntity));
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        GateOutResponse response = gateOutMapper.toResponse(gateOutEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getGateOutNo()).isEqualTo("GOUT-001");
        assertThat(response.getTimestamp()).isEqualTo(LocalDateTime.of(2024, 1, 1, 14, 0));
        assertThat(response.getVehicleNo()).isEqualTo("XYZ-789");
        assertThat(response.getRemarks()).isEqualTo("Test gate out");
        assertThat(response.getStatus()).isEqualTo(GateOutStatus.PENDING);
        assertThat(response.getGateKeeperId()).isEqualTo(1L);
        assertThat(response.getGateKeeperName()).isEqualTo("John Keeper");
        assertThat(response.getStoreKeeperId()).isEqualTo(1L);
        assertThat(response.getStoreKeeperName()).isEqualTo("Jane Store");
        assertThat(response.getDestinationWarehouseId()).isEqualTo(1L);
        assertThat(response.getDestinationWarehouseName()).isEqualTo("Main Warehouse");
        assertThat(response.getCustomerName()).isEqualTo("Test Customer");
        assertThat(response.getCustomerAddress()).isEqualTo("456 Customer Ave");
        assertThat(response.getLines()).hasSize(1);
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        GateOutResponse response = gateOutMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullGateKeeper_ShouldHandleGracefully() {
        // Given
        gateOutEntity.setGateKeeper(null);

        // When
        GateOutResponse response = gateOutMapper.toResponse(gateOutEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getGateKeeperId()).isNull();
        assertThat(response.getGateKeeperName()).isNull();
    }

    @Test
    void toResponse_WithNullStoreKeeper_ShouldHandleGracefully() {
        // Given
        gateOutEntity.setStoreKeeper(null);

        // When
        GateOutResponse response = gateOutMapper.toResponse(gateOutEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStoreKeeperId()).isNull();
        assertThat(response.getStoreKeeperName()).isNull();
    }

    @Test
    void toResponse_WithNullDestinationWarehouse_ShouldHandleGracefully() {
        // Given
        gateOutEntity.setDestinationWarehouse(null);

        // When
        GateOutResponse response = gateOutMapper.toResponse(gateOutEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getDestinationWarehouseId()).isNull();
        assertThat(response.getDestinationWarehouseName()).isNull();
    }

    @Test
    void mapLineToResponse_ShouldMapLineEntityToResponse() {
        // When
        GateOutLineResponse lineResponse = gateOutMapper.mapLineToResponse(gateOutLineEntity);

        // Then
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getItemId()).isEqualTo(1L);
        assertThat(lineResponse.getItemName()).isEqualTo("Widget");
        assertThat(lineResponse.getRfidTagCode()).isEqualTo("RFID-123");
        assertThat(lineResponse.getQty()).isEqualTo(50L);
        assertThat(lineResponse.getFromLocationId()).isEqualTo(1L);
        assertThat(lineResponse.getFromLocationName()).isEqualTo("Bin A-01");
        assertThat(lineResponse.getReason()).isEqualTo(GateOutReason.SALES);
    }

    @Test
    void mapLineToResponse_WithNullLine_ShouldReturnNull() {
        // When
        GateOutLineResponse lineResponse = gateOutMapper.mapLineToResponse(null);

        // Then
        assertThat(lineResponse).isNull();
    }

    @Test
    void mapLineToResponse_WithNullLocation_ShouldHandleGracefully() {
        // Given
        gateOutLineEntity.setFromLocation(null);

        // When
        GateOutLineResponse lineResponse = gateOutMapper.mapLineToResponse(gateOutLineEntity);

        // Then
        assertThat(lineResponse).isNotNull();
        assertThat(lineResponse.getFromLocationId()).isNull();
        assertThat(lineResponse.getFromLocationName()).isNull();
    }
}
