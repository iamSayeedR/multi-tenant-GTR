package com.example.multi_tanent.warehouse.mapper;

import com.example.multi_tanent.warehouse.entity.ProjectEntity;
import com.example.multi_tanent.warehouse.model.ProjectRequest;
import com.example.multi_tanent.warehouse.model.ProjectResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ProjectMapperTest {

    @InjectMocks
    private ProjectMapper projectMapper;

    private ProjectEntity projectEntity;
    private ProjectRequest projectRequest;

    @BeforeEach
    void setUp() {
        projectEntity = new ProjectEntity();
        projectEntity.setId(1L);
        projectEntity.setName("Warehouse Expansion");
        projectEntity.setCode("PRJ-001");
        projectEntity.setDescription("Expand warehouse capacity");
        projectEntity.setStartDate(LocalDate.of(2024, 1, 1));
        projectEntity.setEndDate(LocalDate.of(2024, 12, 31));
        projectEntity.setStatus("IN_PROGRESS");
        projectEntity.setBudgetAmount(new BigDecimal("1000000.00"));
        projectEntity.setActive(true);

        projectRequest = ProjectRequest.builder()
                .name("Warehouse Expansion")
                .code("PRJ-001")
                .description("Expand warehouse capacity")
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .status("IN_PROGRESS")
                .budgetAmount(new BigDecimal("1000000.00"))
                .build();
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        ProjectResponse response = projectMapper.toResponse(projectEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Warehouse Expansion");
        assertThat(response.getCode()).isEqualTo("PRJ-001");
        assertThat(response.getDescription()).isEqualTo("Expand warehouse capacity");
        assertThat(response.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(response.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(response.getStatus()).isEqualTo("IN_PROGRESS");
        assertThat(response.getBudgetAmount()).isEqualByComparingTo(new BigDecimal("1000000.00"));
        assertThat(response.getActive()).isTrue();
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        ProjectResponse response = projectMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        ProjectEntity entity = projectMapper.toEntity(projectRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("Warehouse Expansion");
        assertThat(entity.getCode()).isEqualTo("PRJ-001");
        assertThat(entity.getDescription()).isEqualTo("Expand warehouse capacity");
        assertThat(entity.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(entity.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(entity.getStatus()).isEqualTo("IN_PROGRESS");
        assertThat(entity.getBudgetAmount()).isEqualByComparingTo(new BigDecimal("1000000.00"));
        assertThat(entity.getActive()).isTrue();
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        ProjectEntity entity = projectMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateAllFields() {
        // Given
        ProjectRequest updateRequest = ProjectRequest.builder()
                .name("Updated Project")
                .code("PRJ-002")
                .description("Updated description")
                .startDate(LocalDate.of(2025, 1, 1))
                .endDate(LocalDate.of(2025, 12, 31))
                .status("COMPLETED")
                .budgetAmount(new BigDecimal("2000000.00"))
                .build();

        // When
        projectMapper.updateEntityFromRequest(projectEntity, updateRequest);

        // Then
        assertThat(projectEntity.getName()).isEqualTo("Updated Project");
        assertThat(projectEntity.getCode()).isEqualTo("PRJ-002");
        assertThat(projectEntity.getDescription()).isEqualTo("Updated description");
        assertThat(projectEntity.getStartDate()).isEqualTo(LocalDate.of(2025, 1, 1));
        assertThat(projectEntity.getEndDate()).isEqualTo(LocalDate.of(2025, 12, 31));
        assertThat(projectEntity.getStatus()).isEqualTo("COMPLETED");
        assertThat(projectEntity.getBudgetAmount()).isEqualByComparingTo(new BigDecimal("2000000.00"));
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = projectEntity.getName();
        ProjectRequest updateRequest = ProjectRequest.builder()
                .name(null)
                .code(null)
                .description(null)
                .startDate(null)
                .endDate(null)
                .status(null)
                .budgetAmount(null)
                .build();

        // When
        projectMapper.updateEntityFromRequest(projectEntity, updateRequest);

        // Then
        assertThat(projectEntity.getName()).isEqualTo(originalName);
    }

    @Test
    void updateEntityFromRequest_WithNullEntity_ShouldNotThrowException() {
        // When/Then
        projectMapper.updateEntityFromRequest(null, projectRequest);
        // Should not throw exception
    }

    @Test
    void updateEntityFromRequest_WithNullRequest_ShouldNotThrowException() {
        // When/Then
        projectMapper.updateEntityFromRequest(projectEntity, null);
        // Should not throw exception
    }
}
