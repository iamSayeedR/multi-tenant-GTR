package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.LocationEntity;
import com.example.multi_tanent.warehouse.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class LocationMapperTest {

    @InjectMocks
    private LocationMapper locationMapper;

    private LocationEntity locationEntity;
    private Location location;

    @BeforeEach
    void setUp() {
        locationEntity = new LocationEntity();
        locationEntity.setId(1L);
        locationEntity.setName("Bin A-01");
        locationEntity.setLocationType("BIN");
        locationEntity.setCode("A-01");
        locationEntity.setDescription("First bin in row A");
        locationEntity.setParentLocationId(10L);
        locationEntity.setWarehouseId(1L);

        location = new Location();
        location.setId(1L);
        location.setName("Bin A-01");
        location.setType("BIN");
        location.setCode("A-01");
        location.setDescription("First bin in row A");
        location.setParentId(10L);
        location.setWarehouseId(1L);
    }

    @Test
    void toDto_ShouldMapEntityToDto() {
        // When
        Location dto = locationMapper.toDto(locationEntity);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Bin A-01");
        assertThat(dto.getType()).isEqualTo("BIN");
        assertThat(dto.getCode()).isEqualTo("A-01");
        assertThat(dto.getDescription()).isEqualTo("First bin in row A");
        assertThat(dto.getParentId()).isEqualTo(10L);
        assertThat(dto.getWarehouseId()).isEqualTo(1L);
    }

    @Test
    void toDto_WithNullEntity_ShouldReturnNull() {
        // When
        Location dto = locationMapper.toDto(null);

        // Then
        assertThat(dto).isNull();
    }

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        // When
        LocationEntity entity = locationMapper.toEntity(location);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Bin A-01");
        assertThat(entity.getLocationType()).isEqualTo("BIN");
        assertThat(entity.getCode()).isEqualTo("A-01");
        assertThat(entity.getDescription()).isEqualTo("First bin in row A");
        assertThat(entity.getParentLocationId()).isEqualTo(10L);
        assertThat(entity.getWarehouseId()).isEqualTo(1L);
    }

    @Test
    void toEntity_WithNullDto_ShouldReturnNull() {
        // When
        LocationEntity entity = locationMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromDto_ShouldUpdateAllFields() {
        // Given
        Location updateDto = new Location();
        updateDto.setName("Bin B-02");
        updateDto.setType("RACK");
        updateDto.setCode("B-02");
        updateDto.setDescription("Updated description");
        updateDto.setParentId(20L);
        updateDto.setWarehouseId(2L);

        // When
        locationMapper.updateEntityFromDto(locationEntity, updateDto);

        // Then
        assertThat(locationEntity.getName()).isEqualTo("Bin B-02");
        assertThat(locationEntity.getLocationType()).isEqualTo("RACK");
        assertThat(locationEntity.getCode()).isEqualTo("B-02");
        assertThat(locationEntity.getDescription()).isEqualTo("Updated description");
        assertThat(locationEntity.getParentLocationId()).isEqualTo(20L);
        assertThat(locationEntity.getWarehouseId()).isEqualTo(2L);
    }

    @Test
    void updateEntityFromDto_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = locationEntity.getName();
        Location updateDto = new Location();
        updateDto.setName(null);
        updateDto.setType(null);
        updateDto.setCode(null);

        // When
        locationMapper.updateEntityFromDto(locationEntity, updateDto);

        // Then
        assertThat(locationEntity.getName()).isEqualTo(originalName);
    }
}
