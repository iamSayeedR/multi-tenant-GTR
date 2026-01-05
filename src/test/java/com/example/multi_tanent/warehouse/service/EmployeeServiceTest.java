package com.example.multi_tanent.warehouse.service;

import com.example.multi_tanent.warehouse.entity.DepartmentEntity;
import com.example.multi_tanent.warehouse.entity.EmployeeEntity;
import com.example.multi_tanent.warehouse.mapper.EmployeeMapper;
import com.example.multi_tanent.warehouse.model.EmployeeRequest;
import com.example.multi_tanent.warehouse.model.EmployeeResponse;
import com.example.multi_tanent.warehouse.repository.DepartmentRepository;
import com.example.multi_tanent.warehouse.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    private EmployeeEntity employeeEntity;
    private EmployeeRequest employeeRequest;
    private EmployeeResponse employeeResponse;
    private DepartmentEntity departmentEntity;

    @BeforeEach
    void setUp() {
        departmentEntity = new DepartmentEntity();
        departmentEntity.setId(1L);
        departmentEntity.setName("IT Department");

        employeeEntity = new EmployeeEntity();
        employeeEntity.setId(1L);
        employeeEntity.setName("John Doe");
        employeeEntity.setEmployeeCode("EMP-001");
        employeeEntity.setEmail("john.doe@example.com");
        employeeEntity.setPhone("1234567890");
        employeeEntity.setDepartment(departmentEntity);
        employeeEntity.setActive(true);

        employeeRequest = EmployeeRequest.builder()
                .name("John Doe")
                .employeeCode("EMP-001")
                .departmentId(1L)
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        employeeResponse = EmployeeResponse.builder()
                .id(1L)
                .name("John Doe")
                .employeeCode("EMP-001")
                .departmentId(1L)
                .departmentName("IT Department")
                .email("john.doe@example.com")
                .phone("1234567890")
                .active(true)
                .build();
    }

    @Test
    void create_ShouldCreateEmployeeSuccessfully() {
        // Given
        when(employeeMapper.toEntity(employeeRequest)).thenReturn(employeeEntity);
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(departmentEntity));
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
        when(employeeMapper.toResponse(employeeEntity)).thenReturn(employeeResponse);

        // When
        EmployeeResponse result = employeeService.create(employeeRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getEmployeeCode()).isEqualTo("EMP-001");

        verify(employeeMapper).toEntity(employeeRequest);
        verify(departmentRepository).findById(1L);
        verify(employeeRepository).save(any(EmployeeEntity.class));
        verify(employeeMapper).toResponse(employeeEntity);
    }

    @Test
    void create_WithoutDepartment_ShouldCreateSuccessfully() {
        // Given
        EmployeeRequest requestWithoutDept = EmployeeRequest.builder()
                .name("John Doe")
                .employeeCode("EMP-001")
                .departmentId(null)
                .email("john.doe@example.com")
                .phone("1234567890")
                .build();

        when(employeeMapper.toEntity(requestWithoutDept)).thenReturn(employeeEntity);
        when(employeeRepository.save(any(EmployeeEntity.class))).thenReturn(employeeEntity);
        when(employeeMapper.toResponse(employeeEntity)).thenReturn(employeeResponse);

        // When
        EmployeeResponse result = employeeService.create(requestWithoutDept);

        // Then
        assertThat(result).isNotNull();
        verify(departmentRepository, never()).findById(any());
    }

    @Test
    void create_WithInvalidDepartmentId_ShouldThrowException() {
        // Given
        when(employeeMapper.toEntity(employeeRequest)).thenReturn(employeeEntity);
        when(departmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> employeeService.create(employeeRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Department not found");

        verify(employeeRepository, never()).save(any());
    }

    @Test
    void listAll_ShouldReturnAllActiveEmployees() {
        // Given
        List<EmployeeEntity> employees = Arrays.asList(employeeEntity);
        when(employeeRepository.findByActiveTrue()).thenReturn(employees);
        when(employeeMapper.toResponse(employeeEntity)).thenReturn(employeeResponse);

        // When
        List<EmployeeResponse> result = employeeService.listAll();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("John Doe");

        verify(employeeRepository).findByActiveTrue();
        verify(employeeMapper).toResponse(employeeEntity);
    }

    @Test
    void getById_ShouldReturnEmployee() {
        // Given
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employeeEntity));
        when(employeeMapper.toResponse(employeeEntity)).thenReturn(employeeResponse);

        // When
        EmployeeResponse result = employeeService.getById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("John Doe");

        verify(employeeRepository).findById(1L);
        verify(employeeMapper).toResponse(employeeEntity);
    }

    @Test
    void getById_WithInvalidId_ShouldThrowException() {
        // Given
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> employeeService.getById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Employee not found");

        verify(employeeRepository).findById(999L);
        verify(employeeMapper, never()).toResponse(any());
    }
}
