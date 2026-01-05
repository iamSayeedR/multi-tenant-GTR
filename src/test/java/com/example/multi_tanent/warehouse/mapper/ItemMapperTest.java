package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ItemEntity;
import com.example.multi_tanent.warehouse.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ItemMapperTest {

    @InjectMocks
    private ItemMapper itemMapper;

    private ItemEntity itemEntity;
    private Item item;

    @BeforeEach
    void setUp() {
        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setCode("ITM-001");
        itemEntity.setName("Widget");
        itemEntity.setDefaultRate(new BigDecimal("100.00"));
        itemEntity.setDefaultVatPercent(new BigDecimal("18.00"));

        item = new Item();
        item.setId(1L);
        item.setCode("ITM-001");
        item.setName("Widget");
        item.setDefaultRate(new BigDecimal("100.00"));
        item.setDefaultVatPercent(new BigDecimal("18.00"));
    }

    @Test
    void toDto_ShouldMapEntityToDto() {
        // When
        Item dto = itemMapper.toDto(itemEntity);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getCode()).isEqualTo("ITM-001");
        assertThat(dto.getName()).isEqualTo("Widget");
        assertThat(dto.getDefaultRate()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(dto.getDefaultVatPercent()).isEqualByComparingTo(new BigDecimal("18.00"));
    }

    @Test
    void toDto_WithNullEntity_ShouldReturnNull() {
        // When
        Item dto = itemMapper.toDto(null);

        // Then
        assertThat(dto).isNull();
    }

    @Test
    void toEntity_ShouldMapDtoToEntity() {
        // When
        ItemEntity entity = itemMapper.toEntity(item);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getCode()).isEqualTo("ITM-001");
        assertThat(entity.getName()).isEqualTo("Widget");
        assertThat(entity.getDefaultRate()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(entity.getDefaultVatPercent()).isEqualByComparingTo(new BigDecimal("18.00"));
    }

    @Test
    void toEntity_WithNullDto_ShouldReturnNull() {
        // When
        ItemEntity entity = itemMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromDto_ShouldUpdateAllFields() {
        // Given
        Item updateDto = new Item();
        updateDto.setCode("ITM-002");
        updateDto.setName("Updated Widget");
        updateDto.setDefaultRate(new BigDecimal("200.00"));
        updateDto.setDefaultVatPercent(new BigDecimal("12.00"));

        // When
        itemMapper.updateEntityFromDto(itemEntity, updateDto);

        // Then
        assertThat(itemEntity.getCode()).isEqualTo("ITM-002");
        assertThat(itemEntity.getName()).isEqualTo("Updated Widget");
        assertThat(itemEntity.getDefaultRate()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(itemEntity.getDefaultVatPercent()).isEqualByComparingTo(new BigDecimal("12.00"));
    }

    @Test
    void updateEntityFromDto_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalCode = itemEntity.getCode();
        Item updateDto = new Item();
        updateDto.setCode(null);
        updateDto.setName(null);
        updateDto.setDefaultRate(null);
        updateDto.setDefaultVatPercent(null);

        // When
        itemMapper.updateEntityFromDto(itemEntity, updateDto);

        // Then
        assertThat(itemEntity.getCode()).isEqualTo(originalCode);
    }

    @Test
    void updateEntityFromDto_WithNullEntity_ShouldNotThrowException() {
        // When/Then
        itemMapper.updateEntityFromDto(null, item);
        // Should not throw exception
    }

    @Test
    void updateEntityFromDto_WithNullDto_ShouldNotThrowException() {
        // When/Then
        itemMapper.updateEntityFromDto(itemEntity, null);
        // Should not throw exception
    }
}
