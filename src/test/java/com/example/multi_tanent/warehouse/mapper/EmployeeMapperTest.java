package com.example.multi_tanent.warehouse.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.multi_tanent.warehouse.entity.DepartmentEntity;
import com.example.multi_tanent.warehouse.entity.EmployeeEntity;
import com.example.multi_tanent.warehouse.model.EmployeeRequest;
import com.example.multi_tanent.warehouse.model.EmployeeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeMapperTest {

    @InjectMocks
    private EmployeeMapper employeeMapper;

    private EmployeeEntity employeeEntity;
    private EmployeeRequest employeeRequest;
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
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        // When
        EmployeeResponse response = employeeMapper.toResponse(employeeEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getEmployeeCode()).isEqualTo("EMP-001");
        assertThat(response.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(response.getPhone()).isEqualTo("1234567890");
        assertThat(response.getDepartmentId()).isEqualTo(1L);
        assertThat(response.getDepartmentName()).isEqualTo("IT Department");
        assertThat(response.getActive()).isTrue();
    }

    @Test
    void toResponse_WithNullEntity_ShouldReturnNull() {
        // When
        EmployeeResponse response = employeeMapper.toResponse(null);

        // Then
        assertThat(response).isNull();
    }

    @Test
    void toResponse_WithNullDepartment_ShouldHandleGracefully() {
        // Given
        employeeEntity.setDepartment(null);

        // When
        EmployeeResponse response = employeeMapper.toResponse(employeeEntity);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getDepartmentId()).isNull();
        assertThat(response.getDepartmentName()).isNull();
    }

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        // When
        EmployeeEntity entity = employeeMapper.toEntity(employeeRequest);

        // Then
        assertThat(entity).isNotNull();
        assertThat(entity.getName()).isEqualTo("John Doe");
        assertThat(entity.getEmployeeCode()).isEqualTo("EMP-001");
        assertThat(entity.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(entity.getPhone()).isEqualTo("1234567890");
        assertThat(entity.getActive()).isTrue();
    }

    @Test
    void toEntity_WithNullRequest_ShouldReturnNull() {
        // When
        EmployeeEntity entity = employeeMapper.toEntity(null);

        // Then
        assertThat(entity).isNull();
    }

    @Test
    void updateEntityFromRequest_ShouldUpdateAllFields() {
        // Given
        EmployeeRequest updateRequest = EmployeeRequest.builder()
                .name("Jane Doe")
                .employeeCode("EMP-002")
                .email("jane.doe@example.com")
                .phone("0987654321")
                .build();

        // When
        employeeMapper.updateEntityFromRequest(employeeEntity, updateRequest);

        // Then
        assertThat(employeeEntity.getName()).isEqualTo("Jane Doe");
        assertThat(employeeEntity.getEmployeeCode()).isEqualTo("EMP-002");
        assertThat(employeeEntity.getEmail()).isEqualTo("jane.doe@example.com");
        assertThat(employeeEntity.getPhone()).isEqualTo("0987654321");
    }

    @Test
    void updateEntityFromRequest_WithNullFields_ShouldNotUpdate() {
        // Given
        String originalName = employeeEntity.getName();
        EmployeeRequest updateRequest = EmployeeRequest.builder()
                .name(null)
                .employeeCode(null)
                .email(null)
                .phone(null)
                .build();

        // When
        employeeMapper.updateEntityFromRequest(employeeEntity, updateRequest);

        // Then
        assertThat(employeeEntity.getName()).isEqualTo(originalName);
    }
}
