package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.InventoryEntity;
import com.example.multi_tanent.warehouse.entity.ItemEntity;
import com.example.multi_tanent.warehouse.entity.LocationEntity;
import com.example.multi_tanent.warehouse.model.Inventory;
import com.example.multi_tanent.warehouse.model.InventoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class InventoryMapperTest {

    @InjectMocks
    private InventoryMapper inventoryMapper;

    private InventoryEntity inventoryEntity;
    private Inventory inventory;
    private LocationEntity locationEntity;
    private ItemEntity itemEntity;

    @BeforeEach
    void setUp() {
        locationEntity = new LocationEntity();
        locationEntity.setId(1L);
        locationEntity.setName("Bin A-01");

        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("Widget");
        itemEntity.setCode("WDG-001");

        inventoryEntity = new InventoryEntity();
        inventoryEntity.setId(1L);
        inventoryEntity.setLocation(locationEntity);
        inventoryEntity.setItem(itemEntity);
        inventoryEntity.setQuantity(100L);

        inventory = new Inventory();
        inventory.setLocationId(1L);
        inventory.setItemId(1L);
        inventory.setQuantity(100L);
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        InventoryResponse response = inventoryMapper.toResponse(inventoryEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getLocationId()).isEqualTo(1L);
        assertThat(response.getLocationName()).isEqualTo("Bin A-01");
        assertThat(response.getItemId()).isEqualTo(1L);
        assertThat(response.getItemName()).isEqualTo("Widget");
        assertThat(response.getQuantity()).isEqualTo(100L);
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        InventoryResponse response = inventoryMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullLocation_ShouldHandleGracefully() {
        // Given
        inventoryEntity.setLocation(null);

        // When
        InventoryResponse response = inventoryMapper.toResponse(inventoryEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getLocationId()).isNull();
        assertThat(response.getLocationName()).isNull();
    }

    @Test
    void toResponse_WithNullItem_ShouldHandleGracefully() {
        // Given
        inventoryEntity.setItem(null);

        // When
        InventoryResponse response = inventoryMapper.toResponse(inventoryEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getItemId()).isNull();
        assertThat(response.getItemName()).isNull();
    }

    @Test
    void toDto_ShouldMapEntityToDto() {
        // When
        Inventory dto = inventoryMapper.toDto(inventoryEntity);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getLocationId()).isEqualTo(1L);
        assertThat(dto.getItemId()).isEqualTo(1L);
        assertThat(dto.getQuantity()).isEqualTo(100L);
    }

    @Test
    void toDto_WithNullEntity_ShouldReturnNull() {
        // When
        Inventory dto = inventoryMapper.toDto(null);

        // Then
        assertThat(dto).isNull();
    }

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        // When
        InventoryEntity entity = inventoryMapper.toEntity(inventory);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getQuantity()).isEqualTo(100L);
    }

    @Test
    void toEntity_WithNullDto_ShouldReturnNull() {
        // When
        InventoryEntity entity = inventoryMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }
}
