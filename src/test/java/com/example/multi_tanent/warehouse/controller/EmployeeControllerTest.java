package com.example.multi_tanent.warehouse.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.multi_tanent.warehouse.model.EmployeeRequest;
import com.example.multi_tanent.warehouse.model.EmployeeResponse;
import com.example.multi_tanent.warehouse.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmployeeService employeeService;

    private EmployeeRequest employeeRequest;
    private EmployeeResponse employeeResponse;

    @BeforeEach
    void setUp() {
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
    void create_ShouldReturnCreatedEmployee() throws Exception {
        // Given
        when(employeeService.create(any(EmployeeRequest.class))).thenReturn(employeeResponse);

        // When/Then
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.employeeCode", is("EMP-001")))
                .andExpect(jsonPath("$.departmentId", is(1)))
                .andExpect(jsonPath("$.departmentName", is("IT Department")))
                .andExpect(jsonPath("$.email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.phone", is("1234567890")))
                .andExpect(jsonPath("$.active", is(true)));
    }

    @Test
    void listAll_ShouldReturnAllEmployees() throws Exception {
        // Given
        List<EmployeeResponse> employees = Arrays.asList(employeeResponse);
        when(employeeService.listAll()).thenReturn(employees);

        // When/Then
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].employeeCode", is("EMP-001")));
    }

    @Test
    void getById_ShouldReturnEmployee() throws Exception {
        // Given
        when(employeeService.getById(1L)).thenReturn(employeeResponse);

        // When/Then
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.employeeCode", is("EMP-001")));
    }

    @Test
    void getById_WithInvalidId_ShouldReturnError() throws Exception {
        // Given
        when(employeeService.getById(999L)).thenThrow(new RuntimeException("Employee not found"));

        // When/Then
        mockMvc.perform(get("/api/employees/999"))
                .andExpect(status().is5xxServerError());
    }
}
