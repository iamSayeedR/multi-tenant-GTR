package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ItemEntity;
import com.example.multi_tanent.warehouse.entity.RFIDTagEntity;
import com.example.multi_tanent.warehouse.model.RFIDTagRequest;
import com.example.multi_tanent.warehouse.model.RFIDTagResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RFIDTagMapperTest {

    @InjectMocks
    private RFIDTagMapper rfidTagMapper;

    private RFIDTagEntity rfidTagEntity;
    private RFIDTagRequest rfidTagRequest;
    private ItemEntity itemEntity;

    @BeforeEach
    void setUp() {
        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setName("Widget");
        itemEntity.setCode("WDG-001");

        rfidTagEntity = new RFIDTagEntity();
        rfidTagEntity.setId(1L);
        rfidTagEntity.setTagCode("RFID-12345");
        rfidTagEntity.setItem(itemEntity);
        rfidTagEntity.setActive(true);

        rfidTagRequest = new RFIDTagRequest();
        rfidTagRequest.setTagCode("RFID-12345");
        rfidTagRequest.setItemId(1L);
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        RFIDTagResponse response = rfidTagMapper.toResponse(rfidTagEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTagCode()).isEqualTo("RFID-12345");
        assertThat(response.getItemId()).isEqualTo(1L);
        assertThat(response.getItemCode()).isEqualTo("WDG-001");
        assertThat(response.getItemName()).isEqualTo("Widget");
        assertThat(response.getActive()).isTrue();
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        RFIDTagResponse response = rfidTagMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullItem_ShouldHandleGracefully() {
        // Given
        rfidTagEntity.setItem(null);

        // When
        RFIDTagResponse response = rfidTagMapper.toResponse(rfidTagEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getItemId()).isNull();
        assertThat(response.getItemCode()).isNull();
        assertThat(response.getItemName()).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        RFIDTagEntity entity = rfidTagMapper.toEntity(rfidTagRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getTagCode()).isEqualTo("RFID-12345");
        assertThat(entity.getActive()).isTrue();
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        RFIDTagEntity entity = rfidTagMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateTagCode() {
        // Given
        RFIDTagRequest updateRequest = new RFIDTagRequest();
        updateRequest.setTagCode("RFID-67890");

        // When
        rfidTagMapper.updateEntityFromRequest(rfidTagEntity, updateRequest);

        // Then
        assertThat(rfidTagEntity.getTagCode()).isEqualTo("RFID-67890");
    }

    @Test
    void updateEntityFromRequest_WithNullTagCode_ShouldNotUpdate() {
        // Given
        String originalTagCode = rfidTagEntity.getTagCode();
        RFIDTagRequest updateRequest = new RFIDTagRequest();
        updateRequest.setTagCode(null);

        // When
        rfidTagMapper.updateEntityFromRequest(rfidTagEntity, updateRequest);

        // Then
        assertThat(rfidTagEntity.getTagCode()).isEqualTo(originalTagCode);
    }

    @Test
    void updateEntityFromRequest_WithNullEntity_ShouldNotThrowException() {
        // When/Then
        rfidTagMapper.updateEntityFromRequest(null, rfidTagRequest);
        // Should not throw exception
    }

    @Test
    void updateEntityFromRequest_WithNullRequest_ShouldNotThrowException() {
        // When/Then
        rfidTagMapper.updateEntityFromRequest(rfidTagEntity, null);
        // Should not throw exception
    }
}
