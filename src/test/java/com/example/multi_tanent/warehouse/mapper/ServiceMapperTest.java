package com.example.multi_tanent.warehouse.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.multi_tanent.warehouse.entity.ServiceEntity;
import com.example.multi_tanent.warehouse.model.ServiceRequest;
import com.example.multi_tanent.warehouse.model.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceMapperTest {

    @InjectMocks
    private ServiceMapper serviceMapper;

    private ServiceEntity serviceEntity;
    private ServiceRequest serviceRequest;

    @BeforeEach
    void setUp() {
        serviceEntity = new ServiceEntity();
        serviceEntity.setId(1L);
        serviceEntity.setName("Maintenance Service");
        serviceEntity.setCode("SVC-001");
        serviceEntity.setDescription("Regular maintenance");
        serviceEntity.setDefaultUom("Hour");
        serviceEntity.setCategory("Maintenance");
        serviceEntity.setActive(true);

        serviceRequest = ServiceRequest.builder()
                .name("Maintenance Service")
                .code("SVC-001")
                .description("Regular maintenance")
                .defaultUom("Hour")
                .category("Maintenance")
                .build();
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        ServiceResponse response = serviceMapper.toResponse(serviceEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Maintenance Service");
        assertThat(response.getCode()).isEqualTo("SVC-001");
        assertThat(response.getDescription()).isEqualTo("Regular maintenance");
        assertThat(response.getDefaultUom()).isEqualTo("Hour");
        assertThat(response.getCategory()).isEqualTo("Maintenance");
        assertThat(response.getActive()).isTrue();
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        ServiceResponse response = serviceMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        ServiceEntity entity = serviceMapper.toEntity(serviceRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Maintenance Service");
        assertThat(entity.getCode()).isEqualTo("SVC-001");
        assertThat(entity.getDescription()).isEqualTo("Regular maintenance");
        assertThat(entity.getDefaultUom()).isEqualTo("Hour");
        assertThat(entity.getCategory()).isEqualTo("Maintenance");
        assertThat(entity.getActive()).isTrue();
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        ServiceEntity entity = serviceMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateAllFields() {
        // Given
        ServiceRequest updateRequest = ServiceRequest.builder()
                .name("Updated Service")
                .code("SVC-002")
                .description("Updated description")
                .defaultUom("Day")
                .category("Repair")
                .build();

        // When
        serviceMapper.updateEntityFromRequest(serviceEntity, updateRequest);

        // Then
        assertThat(serviceEntity.getName()).isEqualTo("Updated Service");
        assertThat(serviceEntity.getCode()).isEqualTo("SVC-002");
        assertThat(serviceEntity.getDescription()).isEqualTo("Updated description");
        assertThat(serviceEntity.getDefaultUom()).isEqualTo("Day");
        assertThat(serviceEntity.getCategory()).isEqualTo("Repair");
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = serviceEntity.getName();
        ServiceRequest updateRequest = ServiceRequest.builder()
                .name(null)
                .code(null)
                .description(null)
                .defaultUom(null)
                .category(null)
                .build();

        // When
        serviceMapper.updateEntityFromRequest(serviceEntity, updateRequest);

        // Then
        assertThat(serviceEntity.getName()).isEqualTo(originalName);
    }

    @Test
    void updateEntityFromRequest_WithNullEntity_ShouldNotThrowException() {
        // When/Then
        serviceMapper.updateEntityFromRequest(null, serviceRequest);
        // Should not throw exception
    }

    @Test
    void updateEntityFromRequest_WithNullRequest_ShouldNotThrowException() {
        // When/Then
        serviceMapper.updateEntityFromRequest(serviceEntity, null);
        // Should not throw exception
    }
}
