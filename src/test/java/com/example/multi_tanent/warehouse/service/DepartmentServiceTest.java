package com.example.multi_tanent.warehouse.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.example.multi_tanent.warehouse.entity.DepartmentEntity;
import com.example.multi_tanent.warehouse.entity.WarehouseEntity;
import com.example.multi_tanent.warehouse.mapper.DepartmentMapper;
import com.example.multi_tanent.warehouse.model.DepartmentRequest;
import com.example.multi_tanent.warehouse.model.DepartmentResponse;
import com.example.multi_tanent.warehouse.repository.DepartmentRepository;
import com.example.multi_tanent.warehouse.repository.WarehouseRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentService departmentService;

    private DepartmentEntity departmentEntity;
    private DepartmentRequest departmentRequest;
    private DepartmentResponse departmentResponse;
    private WarehouseEntity warehouseEntity;

    @BeforeEach
    void setUp() {
        warehouseEntity = new WarehouseEntity();
        warehouseEntity.setId(1L);
        warehouseEntity.setName("Main Warehouse");

        departmentEntity = new DepartmentEntity();
        departmentEntity.setId(1L);
        departmentEntity.setName("IT Department");
        departmentEntity.setCode("IT-001");
        departmentEntity.setType("DEPARTMENT");
        departmentEntity.setWarehouse(warehouseEntity);
        departmentEntity.setActive(true);

        departmentRequest = DepartmentRequest.builder()
                .name("IT Department")
                .code("IT-001")
                .type("DEPARTMENT")
                .warehouseId(1L)
                .build();

        departmentResponse = DepartmentResponse.builder()
                .id(1L)
                .name("IT Department")
                .code("IT-001")
                .type("DEPARTMENT")
                .warehouseId(1L)
                .warehouseName("Main Warehouse")
                .active(true)
                .build();
    }

    @Test
    void ShouldCreateDepartmentSuccessfully() {
        // Given
        when(departmentMapper.toEntity(departmentRequest)).thenReturn(departmentEntity);
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouseEntity));
        when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(departmentEntity);
        when(departmentMapper.toResponse(departmentEntity)).thenReturn(departmentResponse);

        // When
        DepartmentResponse result = departmentService.create(departmentRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("IT Department");
        assertThat(result.getCode()).isEqualTo("IT-001");

        verify(departmentMapper).toEntity(departmentRequest);
        verify(warehouseRepository).findById(1L);
        verify(departmentRepository).save(any(DepartmentEntity.class));
        verify(departmentMapper).toResponse(departmentEntity);
    }

    @Test
    void ShouldCreateWithoutWarehouseSuccessfully() {
        // Given
        DepartmentRequest requestWithoutWarehouse = DepartmentRequest.builder()
                .name("IT Department")
                .code("IT-001")
                .type("DEPARTMENT")
                .warehouseId(null)
                .build();

        when(departmentMapper.toEntity(requestWithoutWarehouse)).thenReturn(departmentEntity);
        when(departmentRepository.save(any(DepartmentEntity.class))).thenReturn(departmentEntity);
        when(departmentMapper.toResponse(departmentEntity)).thenReturn(departmentResponse);

        // When
        DepartmentResponse result = departmentService.create(requestWithoutWarehouse);

        // Then
        assertThat(result).isNotNull();
        verify(warehouseRepository, never()).findById(any());
    }

    @Test
    void ShouldThrowExceptionWhenWarehouseIdIsInvalid() {
        // Given
        when(departmentMapper.toEntity(departmentRequest)).thenReturn(departmentEntity);
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> departmentService.create(departmentRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Warehouse not found");

        verify(departmentRepository, never()).save(any());
    }

    @Test
    void ShouldReturnAllActiveDepartments() {
        // Given
        List<DepartmentEntity> departments = Collections.singletonList(departmentEntity);
        when(departmentRepository.findByActiveTrue()).thenReturn(departments);
        when(departmentMapper.toResponse(departmentEntity)).thenReturn(departmentResponse);

        // When
        List<DepartmentResponse> result = departmentService.listAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("IT Department");

        verify(departmentRepository).findByActiveTrue();
        verify(departmentMapper).toResponse(departmentEntity);
    }

    @Test
    void ShouldReturnEmptyListWithNoDepartment() {
        // Given
        when(departmentRepository.findByActiveTrue()).thenReturn(List.of());

        // When
        List<DepartmentResponse> result = departmentService.listAll();

        // Then
        assertThat(result).isEmpty();
        verify(departmentRepository).findByActiveTrue();
    }

    @Test
    void ShouldReturnDepartmentById() {
        // Given
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(departmentEntity));
        when(departmentMapper.toResponse(departmentEntity)).thenReturn(departmentResponse);

        // When
        DepartmentResponse result = departmentService.getById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("IT Department");

        verify(departmentRepository).findById(1L);
        verify(departmentMapper).toResponse(departmentEntity);
    }

    @Test
    void getById_WithInvalidId_ShouldThrowException() {
        // Given
        when(departmentRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> departmentService.getById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Department not found");

        verify(departmentRepository).findById(999L);
        verify(departmentMapper, never()).toResponse(any());
    }
}
